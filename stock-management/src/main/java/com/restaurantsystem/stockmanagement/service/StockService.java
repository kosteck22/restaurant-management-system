package com.restaurantsystem.stockmanagement.service;

import com.restaurantsystem.stockmanagement.entity.*;
import com.restaurantsystem.stockmanagement.exception.RequestValidationException;
import com.restaurantsystem.stockmanagement.exception.ResourceNotFoundException;
import com.restaurantsystem.stockmanagement.service.dao.ProductRepository;
import com.restaurantsystem.stockmanagement.service.dao.StockRepository;
import com.restaurantsystem.stockmanagement.service.dao.StockTransactionRepository;
import com.restaurantsystem.stockmanagement.web.client.InvoiceManagementClient;
import com.restaurantsystem.stockmanagement.web.dto.addToStock.ProductDto;
import com.restaurantsystem.stockmanagement.web.dto.addToStock.AddToStockRequest;
import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.DeduceFromStockRequest;
import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.ProductToDeduceDto;
import com.restaurantsystem.stockmanagement.web.dto.invoice.InvoiceDto;
import com.restaurantsystem.stockmanagement.web.dto.invoice.OrderDetailsDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockService implements IStockService {
    private final InvoiceManagementClient invoiceManagementClient;
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void addProductsToStock(AddToStockRequest stockRequest) {
        if (SourceType.INVOICE.equals(stockRequest.stockSource().sourceType())) {
            if (stockRequest.stockSource().getSourceId().isEmpty()) {
                throw new RequestValidationException("For Source type equals invoice invoiceId is required");
            }

            InvoiceDto invoiceDto = invoiceManagementClient.get(stockRequest.stockSource().sourceId()).getBody();
            assert invoiceDto != null;

            Map<String, ProductDto> productsFromRequest = stockRequest.products().stream()
                    .collect(Collectors.toMap(
                            ProductDto::name,
                            p -> new ProductDto(p.name(), p.quantity(), p.unitOfMeasure(), p.category()),
                            (p1, p2) -> new ProductDto(p1.name(), p1.quantity().add(p2.quantity()), p1.unitOfMeasure(), p1.category())
                    ));

            Map<String, BigDecimal> productsFromInvoice = invoiceDto.order().orderDetails().stream()
                    .collect(Collectors.toMap(
                            p -> p.product().name(),
                            OrderDetailsDto::quantity,
                            BigDecimal::add
                    ));

            //checking if requested product is present and its quantity is not greater than available in invoice
            productsFromRequest.forEach((productName, productDto) -> {
                BigDecimal invoiceQuantity = productsFromInvoice.get(productName);
                if (invoiceQuantity == null || productDto.quantity().compareTo(invoiceQuantity) > 0) {
                    throw new RequestValidationException("Product %s quantity is greater than available in the invoice or not present in the invoice.".formatted(productName));
                }
            });

            List<Product> newProductsToSave = new ArrayList<>();
            Map<String, String> productNameToIdMap = new HashMap<>();

            productsFromRequest.forEach((productName, productDto) -> {
                Optional<Product> productFromDB = productRepository.findByName(productName);
                if (productFromDB.isPresent()) {
                    productNameToIdMap.put(productName, productFromDB.get().getName());

                } else {
                    Product productToSave = Product.builder()
                            .name(productName)
                            .unitOfMeasure(productDto.unitOfMeasure())
                            .category(productDto.category())
                            .build();

                    newProductsToSave.add(productToSave);
                }
            });

            if (!newProductsToSave.isEmpty()) {
                productRepository.saveAll(newProductsToSave).forEach(p ->
                        productNameToIdMap.put(p.getName(), p.getId()));
            }

            List<ProductDetail> productDetails = new ArrayList<>();
            List<Stock> stocksToUpdateOrCreate = new ArrayList<>();

            productsFromRequest.forEach((productName, productDto) -> {
                String productId = productNameToIdMap.get(productName);

                productDetails.add(ProductDetail.builder()
                        .productId(productId)
                        .quantity(productDto.quantity())
                        .build());

                Stock existingStock = stockRepository.findByProductId(productId).orElse(null);
                if (existingStock != null) {
                    existingStock.setQuantity(existingStock.getQuantity().add(productDto.quantity()));
                    stocksToUpdateOrCreate.add(existingStock);
                } else {
                    Stock newStock = Stock.builder()
                            .productId(productId)
                            .quantity(productDto.quantity())
                            .build();
                    stocksToUpdateOrCreate.add(newStock);
                }
            });

            StockTransaction stockTransaction = StockTransaction.builder()
                    .date(stockRequest.date())
                    .description(stockRequest.description())
                    .productDetails(productDetails)
                    .source(StockSource.builder()
                            .sourceId(stockRequest.stockSource().sourceId())
                            .sourceType(stockRequest.stockSource().sourceType())
                            .build()
                    )
                    .transactionType(TransactionType.ADD)
                    .build();

            stockTransactionRepository.save(stockTransaction);
            stockRepository.saveAll(stocksToUpdateOrCreate);
        } else {
            //implementation for other and gift source
        }
    }

    @Override
    @Transactional
    public void deduceProductsFromStock(DeduceFromStockRequest stockRequest) {
        List<ProductToDeduceDto> products = stockRequest.products();
        List<String> productIdsFromRequest = products.stream()
                .map(ProductToDeduceDto::productId)
                .toList();

        List<Stock> stocks = stockRepository.findAllByProductIdIn(productIdsFromRequest);
        List<String> productIdsFromDb = stocks.stream()
                .map(Stock::getProductId)
                .toList();

        List<String> missingIds = productIdsFromRequest.stream()
                .filter(id -> !productIdsFromDb.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ResourceNotFoundException("There are no products in stock for given ids [%s]".formatted(String.join(",", missingIds)));
        }

        Map<String, BigDecimal> productIdToQuantityMap = products.stream()
                .collect(Collectors.toMap(ProductToDeduceDto::productId, ProductToDeduceDto::quantity));

        List<Stock> stocksWithNotEnoughQuantity = stocks.stream()
                .filter(s -> s.getQuantity()
                        .compareTo(productIdToQuantityMap.get(s.getProductId())) >= 0)
                .toList();

        if (!stocksWithNotEnoughQuantity.isEmpty()) {
            String productsIds = stocksWithNotEnoughQuantity.stream()
                    .map(Stock::getProductId)
                    .collect(Collectors.joining(","));
            throw new RequestValidationException("There are products with not enough quantity [%s]".formatted(String.join(",", productsIds)));
        }

        List<ProductDetail> productDetails = stockRequest.products().stream()
                .map(p -> ProductDetail.builder()
                        .productId(p.productId())
                        .quantity(p.quantity())
                        .build())
                .toList();

        StockTransaction stockTransaction = StockTransaction.builder()
                .date(stockRequest.date())
                .description(stockRequest.description())
                .productDetails(productDetails)
                .source(StockSource.builder()
                        .sourceId(stockRequest.stockSource().sourceId())
                        .sourceType(stockRequest.stockSource().sourceType())
                        .build()
                )
                .transactionType(TransactionType.DEDUCE)
                .build();

        stocks.forEach(s -> s.setQuantity(
                s.getQuantity().subtract(productIdToQuantityMap.get(
                        s.getProductId()))));


        stockTransactionRepository.save(stockTransaction);
        stockRepository.saveAll(stocks);
    }
}
