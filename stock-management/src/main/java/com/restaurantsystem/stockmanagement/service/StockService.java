package com.restaurantsystem.stockmanagement.service;

import com.restaurantsystem.stockmanagement.entity.*;
import com.restaurantsystem.stockmanagement.exception.RequestValidationException;
import com.restaurantsystem.stockmanagement.exception.ResourceNotFoundException;
import com.restaurantsystem.stockmanagement.service.dao.StockAuditRepository;
import com.restaurantsystem.stockmanagement.service.dao.ProductRepository;
import com.restaurantsystem.stockmanagement.service.dao.StockRepository;
import com.restaurantsystem.stockmanagement.service.dao.StockTransactionRepository;
import com.restaurantsystem.stockmanagement.web.client.InvoiceManagementClient;
import com.restaurantsystem.stockmanagement.web.client.RecipeClient;
import com.restaurantsystem.stockmanagement.web.dto.InventoryRequest;
import com.restaurantsystem.stockmanagement.web.dto.SaleCreatedEvent;
import com.restaurantsystem.stockmanagement.web.dto.addToStock.ProductDto;
import com.restaurantsystem.stockmanagement.web.dto.addToStock.AddToStockRequest;
import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.DeduceFromStockRequest;
import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.ProductDetailDto;
import com.restaurantsystem.stockmanagement.web.dto.invoice.InvoiceDto;
import com.restaurantsystem.stockmanagement.web.dto.invoice.OrderDetailsDto;
import com.restaurantsystem.stockmanagement.web.dto.recipe.IngredientDto;
import com.restaurantsystem.stockmanagement.web.dto.recipe.RecipeDto;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class StockService implements IStockService {
    private final InvoiceManagementClient invoiceManagementClient;
    private final RecipeClient recipeClient;
    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final StockRepository stockRepository;
    private final StockAuditRepository stockAuditRepository;

    @Override
    public Page<Stock> getStocksAsPage(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

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
                            .productName(productName)
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
            //implementation for gift and other source
        }
    }

    @Override
    @Transactional
    public void deduceProductsFromStock(DeduceFromStockRequest stockRequest) {
        List<ProductDetailDto> products = stockRequest.products();
        List<String> productIdsFromRequest = products.stream()
                .map(ProductDetailDto::productId)
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
                .collect(Collectors.toMap(
                        ProductDetailDto::productId,
                        ProductDetailDto::quantity,
                        BigDecimal::add));

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
                .collect(Collectors.groupingBy(
                        ProductDetailDto::productId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                ProductDetailDto::quantity,
                                BigDecimal::add
                        )))
                .entrySet().stream()
                .map(entry -> ProductDetail.builder()
                        .productId(entry.getKey())
                        .quantity(entry.getValue())
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

    @Override
    @Transactional
    public String addStockCheckList(InventoryRequest inventoryRequest) {
        boolean isNewerStockChecklist = stockAuditRepository.existsByTypeAndDateGreaterThan(StockAuditType.STOCK_CHECKLIST, inventoryRequest.date());
        if (isNewerStockChecklist) {
            throw new ValidationException("Another Stock audit exists that was created after yours.");
        }

        Map<String, BigDecimal> productIdToQuantityMapFromRequest = inventoryRequest.products().stream()
                .collect(Collectors.toMap(
                        ProductDetailDto::productId,
                        ProductDetailDto::quantity,
                        BigDecimal::add
                ));

        List<Stock> stocks = stockRepository.findAll();
        Map<String, BigDecimal> productIdToQuantityMapFromStock = stocks.stream()
                .collect(Collectors.toMap(
                        Stock::getProductId,
                        Stock::getQuantity,
                        BigDecimal::add
                ));

        List<String> missingIds = productIdToQuantityMapFromRequest.keySet().stream()
                .filter(id -> !productIdToQuantityMapFromStock.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ResourceNotFoundException("There are no productActual in stock for given ids [%s]".formatted(String.join(",", missingIds)));
        }

        Optional<StockAudit> latestStockCheckList = stockAuditRepository.findTopByStockAuditTypeAndOrderByDateDesc(StockAuditType.STOCK_CHECKLIST);
        List<StockTransaction> stockTransactions;
        Map<String, BigDecimal> productIdToQuantityMapFromDb;

        if (latestStockCheckList.isPresent()) {
            stockTransactions = stockTransactionRepository.findByDateGreaterThanAndTransactionType(latestStockCheckList.get().getDate(), TransactionType.ADD);
            productIdToQuantityMapFromDb = Stream.concat(
                            latestStockCheckList.get().getProductDetails().stream(),
                            stockTransactions.stream()
                                    .flatMap(t -> t.getProductDetails().stream())
                    )
                    .collect(Collectors.groupingBy(
                            ProductDetail::getProductId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    ProductDetail::getQuantity,
                                    BigDecimal::add))
                    );
        } else {
            stockTransactions = stockTransactionRepository.findByTransactionType(TransactionType.ADD);
            productIdToQuantityMapFromDb = stockTransactions.stream()
                    .flatMap(t -> t.getProductDetails().stream())
                    .collect(Collectors.groupingBy(
                            ProductDetail::getProductId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    ProductDetail::getQuantity,
                                    BigDecimal::add)));
        }

        List<String> productIdsForExceedingQuantity = productIdToQuantityMapFromRequest.entrySet().stream()
                .filter(entry -> {
                    String productId = entry.getKey();
                    BigDecimal quantity = entry.getValue();
                    BigDecimal quantityFromDb = productIdToQuantityMapFromDb.getOrDefault(productId, BigDecimal.ZERO);

                    return quantity.compareTo(quantityFromDb) > 0;
                })
                .map(Map.Entry::getKey)
                .toList();

        if (!productIdsForExceedingQuantity.isEmpty()) {
            throw new ValidationException("Quantity for product ids [%s] cannot be greater that from db".formatted(String.join(",", productIdsForExceedingQuantity)));
        }

        List<ProductDetail> productActual = productIdToQuantityMapFromRequest
                .entrySet().stream()
                .map(entry -> ProductDetail.builder()
                        .productId(entry.getKey())
                        .quantity(entry.getValue())
                        .build())
                .toList();

        List<ProductDetail> productUsage = productIdToQuantityMapFromDb.entrySet().stream()
                .map(entry -> ProductDetail.builder()
                        .productId(entry.getKey())
                        .quantity(entry.getValue().subtract(productIdToQuantityMapFromRequest.get(entry.getKey())))
                        .build())
                .toList();

        List<ProductDetail> productDiff = calculateDiff(productIdToQuantityMapFromRequest, productIdToQuantityMapFromStock).entrySet().stream()
                .map(entry -> ProductDetail.builder()
                        .productId(entry.getKey())
                        .quantity(entry.getValue().subtract(productIdToQuantityMapFromRequest.get(entry.getKey())))
                        .build())
                .toList();

        StockAudit stockCheckList = StockAudit.builder()
                .date(inventoryRequest.date())
                .description(inventoryRequest.description())
                .type(StockAuditType.STOCK_CHECKLIST)
                .productDetails(productActual)
                .build();

        StockAudit stockDiff = StockAudit.builder()
                .date(inventoryRequest.date())
                .type(StockAuditType.STOCK_DIFF_FROM_CALC)
                .productDetails(productDiff)
                .build();


        StockAudit stockUsage = StockAudit.builder()
                .date(inventoryRequest.date())
                .type(StockAuditType.STOCK_USAGE_FROM_LAST_CHECK)
                .productDetails(productUsage)
                .build();

        List<Stock> stocksToSave = productActual.stream()
                .map(p -> Stock.builder()
                        .productId(p.getProductId())
                        .productName(stocks.stream()
                                .filter(s -> s.getProductId().equals(p.getProductId()))
                                .findFirst()
                                .map(Stock::getProductName)
                                .orElse("Default"))
                        .quantity(p.getQuantity())
                        .build())
                .toList();

        stockRepository.deleteAll();
        stockRepository.saveAll(stocksToSave);
        stockAuditRepository.saveAll(List.of(stockDiff, stockUsage));

        return stockAuditRepository.save(stockCheckList).getId();
    }

    private Map<String, BigDecimal> calculateDiff(Map<String, BigDecimal> realInventory, Map<String, BigDecimal> calculatedInventory) {
        Map<String, BigDecimal> result = new HashMap<>();

        realInventory.forEach((productId, quantity) -> {
            BigDecimal calculatedQuantity = calculatedInventory.getOrDefault(productId, BigDecimal.ZERO);
            result.put(productId, quantity.subtract(calculatedQuantity));
        });

        calculatedInventory.forEach((productId, quantity) -> {
            if (!realInventory.containsKey(productId)) {
                result.put(productId, quantity.negate());
            }
        });

        return result;
    }

    @KafkaListener(topics = "saleCreated", groupId = "saleGroup")
    public void consumeSaleCreatedEvent(SaleCreatedEvent event) {
        List<RecipeDto> recipes = fetchAndPrepareRecipes(event);

        List<IngredientDto> aggregatedIngredients = recipes.stream()
                .flatMap(r -> r.ingredients().stream())
                .collect(Collectors.toMap(
                        IngredientDto::name,
                        i -> i,
                        (i1, i2) -> IngredientDto.builder()
                                .name(i1.name())
                                .unitOfMeasure(i1.unitOfMeasure())
                                .quantity(i1.quantity().add(i2.quantity()))
                                .build())
                ).values().stream().toList();

        List<Stock> stocks = stockRepository.findAll();
        List<ProductDetail> productDetails = new ArrayList<>();

        aggregatedIngredients
                .forEach(i -> {
                    BigDecimal ingredientQuantityNeeded = i.quantity();
                    List<Stock> stockForIngredient = stocks.stream()
                            .filter(s -> s.getProductName().contains(i.name()))
                            .toList();

                    for (Stock stock : stockForIngredient) {
                        if (ingredientQuantityNeeded.compareTo(BigDecimal.ZERO) <= 0) break;

                        BigDecimal quantityToDeduct = stock.getQuantity().min(ingredientQuantityNeeded);
                        productDetails.add(new ProductDetail(stock.getProductId(), quantityToDeduct));
                        stock.setQuantity(stock.getQuantity().subtract(quantityToDeduct));
                        ingredientQuantityNeeded = ingredientQuantityNeeded.subtract(quantityToDeduct);

                    }

                    if (ingredientQuantityNeeded.compareTo(BigDecimal.ZERO) > 0) {
                        throw new ResourceNotFoundException("There is not enough products in Stock for ingredient [%s]".formatted(i.name()));
                    }
                });

        StockTransaction stockTransaction = StockTransaction.builder()
                .date(event.getDate())
                .productDetails(productDetails)
                .source(StockSource.builder()
                        .sourceId(event.getId())
                        .sourceType(SourceType.SALE_REQUEST)
                        .build())
                .transactionType(TransactionType.DEDUCE)
                .build();

        stockTransactionRepository.save(stockTransaction);
        stockRepository.saveAll(stocks);
    }

    private List<RecipeDto> fetchAndPrepareRecipes(SaleCreatedEvent event) {
        List<RecipeDto> recipes = recipeClient.getRecipesByIds(event.getSoldItemsIdToQuantityMap().keySet().stream().toList()).getBody();
        assert recipes != null;

        return recipes.stream()
                .map(recipe -> adjustRecipeQuantities(recipe, event))
                .collect(Collectors.toList());
    }

    private RecipeDto adjustRecipeQuantities(RecipeDto recipe, SaleCreatedEvent event) {
        Integer saleQuantity = event.getSoldItemsIdToQuantityMap().get(recipe.menuItemId());
        if (saleQuantity == null) {
            throw new ResourceNotFoundException("Recipe for menu item [%s] not found".formatted(recipe.menuItemId()));
        }

        List<IngredientDto> adjustedIngredients = recipe.ingredients().stream()
                .map(i -> new IngredientDto(i.name(), i.unitOfMeasure(), i.quantity().multiply(BigDecimal.valueOf(saleQuantity))))
                .collect(Collectors.toList());

        return new RecipeDto(recipe.id(), adjustedIngredients, recipe.menuItemId());
    }

}
