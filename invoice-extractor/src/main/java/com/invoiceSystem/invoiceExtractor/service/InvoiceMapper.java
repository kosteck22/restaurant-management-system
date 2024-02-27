package com.invoiceSystem.invoiceExtractor.service;

import com.invoiceSystem.invoiceExtractor.entity.*;
import com.invoiceSystem.invoiceExtractor.web.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

    public InvoiceResponse toDto(Invoice invoice) {
        return InvoiceResponse.builder()
                .number(invoice.getNumber())
                .createdAt(invoice.getCreatedAt())
                .buyer(getCompanyResponse(invoice.getBuyer()))
                .seller(getCompanyResponse(invoice.getSeller()))
                .order(getOrderResponse(invoice.getOrder()))
                .build();
    }

    private OrderResponse getOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderDetails(getOrderDetailsResponse(order.getOrderDetails()))
                .build();
    }

    private List<OrderDetailsResponse> getOrderDetailsResponse(List<OrderDetails> orderDetails) {
        return orderDetails.stream()
                .map(this::getOrderDetailsResponse)
                .collect(Collectors.toList());
    }

    private OrderDetailsResponse getOrderDetailsResponse(OrderDetails orderDetails) {
        return OrderDetailsResponse.builder()
                .positionNumber(orderDetails.getPositionNumber())
                .product(getProductResponse(orderDetails.getProduct()))
                .discount(orderDetails.getDiscount())
                .quantity(orderDetails.getQuantity())
                .build();
    }

    private ProductResponse getProductResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .netPrice(product.getNetPrice())
                .unitOfMeasure(product.getUnitOfMeasure())
                .vat(product.getVat())
                .build();
    }

    private CompanyResponse getCompanyResponse(Company seller) {
        return CompanyResponse.builder()
                .name(seller.getName())
                .street(seller.getStreet())
                .city(seller.getCity())
                .postalCode(seller.getPostalCode())
                .nip(seller.getNip())
                .regon(seller.getRegon())
                .build();
    }
}
