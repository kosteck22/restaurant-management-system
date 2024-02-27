package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.entity.Invoice;
import com.invoicesystem.invoicemanagement.entity.Order.Order;
import com.invoicesystem.invoicemanagement.entity.Order.OrderDetails;
import com.invoicesystem.invoicemanagement.entity.Order.Product;
import com.invoicesystem.invoicemanagement.entity.Order.VatRate;
import com.invoicesystem.invoicemanagement.web.dto.request.InvoiceRequest;
import com.invoicesystem.invoicemanagement.web.dto.request.OrderDetailsRequest;
import com.invoicesystem.invoicemanagement.web.dto.request.OrderRequest;
import com.invoicesystem.invoicemanagement.web.dto.request.ProductRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceCreatorService {

    public Invoice create(InvoiceRequest request, String buyerCompanyId, String sellerCompanyId) {
        return Invoice.builder()
                .number(request.number())
                .createdAt(request.createdAt())
                .sellerId(sellerCompanyId)
                .buyerId(buyerCompanyId)
                .order(creatOrder(request.order()))
                .build();
    }

    private Order creatOrder(OrderRequest request) {
        List<OrderDetails> orderDetails = createOrderDetails(request.orderDetails());
        BigDecimal netTotal = BigDecimal.ZERO;
        BigDecimal vatTotal = BigDecimal.ZERO;
        BigDecimal grossTotal = BigDecimal.ZERO;

        for (OrderDetails orderDetail : orderDetails) {
            netTotal = netTotal.add(orderDetail.getNetTotal());
            vatTotal = vatTotal.add(orderDetail.getVatTotal());
            grossTotal = grossTotal.add(orderDetail.getGrossPriceTotal());
        }

        return Order.builder()
                .orderDetails(orderDetails)
                .netPriceTotal(netTotal)
                .vatTotal(vatTotal)
                .grossPriceTotal(grossTotal)
                .build();
    }

    private List<OrderDetails> createOrderDetails(List<OrderDetailsRequest> request) {
        return request.stream()
                .map(this::createOrderDetails)
                .collect(Collectors.toList());
    }

    private OrderDetails createOrderDetails(OrderDetailsRequest request) {
        Product product = createProduct(request.product());
        BigDecimal netTotal = getNetTotal(product.getNetPrice(), request.discount(), request.quantity());
        BigDecimal grossTotal = getGrossPrice(netTotal, product.getVat().getRate());

        return OrderDetails.builder()
                .positionNumber(request.positionNumber())
                .product(product)
                .quantity(request.quantity())
                .discount(request.discount())
                .netTotal(netTotal)
                .vatTotal(grossTotal.subtract(netTotal))
                .grossPriceTotal(grossTotal)
                .build();
    }

    private Product createProduct(ProductRequest request) {
        return Product.builder()
                .name(request.name())
                .unitOfMeasure(request.unitOfMeasure())
                .netPrice(request.netPrice())
                .vat(VatRate.valueOf(request.vat()))
                .grossPrice(getGrossPrice(request.netPrice(), request.vat()))
                .build();
    }

    private BigDecimal getGrossPrice(BigDecimal netPrice, Integer vat) {
        BigDecimal vatMulti = getVatMulti(vat);
        BigDecimal grossPrice = netPrice.multiply(vatMulti);

        return grossPrice.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getVatMulti(Integer vat) {
        return BigDecimal.valueOf(vat).divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE);
    }

    private BigDecimal getNetTotal(BigDecimal netPrice, Integer discount, BigDecimal quantity) {
        BigDecimal discountMulti = getDiscountMulti(discount);
        return netPrice.multiply(discountMulti).multiply(quantity).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getDiscountMulti(Integer discount) {
        return discount == null ? BigDecimal.ONE : BigDecimal.ONE.subtract(BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100)));
    }
}
