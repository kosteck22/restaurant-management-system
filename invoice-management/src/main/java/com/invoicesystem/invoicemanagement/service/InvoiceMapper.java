package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.entity.Invoice;
import com.invoicesystem.invoicemanagement.entity.Order.Order;
import com.invoicesystem.invoicemanagement.entity.Order.OrderDetails;
import com.invoicesystem.invoicemanagement.entity.Order.Product;
import com.invoicesystem.invoicemanagement.web.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {
    public InvoiceDto toDto(Invoice invoice, CompanyDto buyer, CompanyDto seller) {
        return InvoiceDto.builder()
                .id(invoice.getId())
                .number(invoice.getNumber())
                .createdAt(invoice.getCreatedAt())
                .buyer(buyer)
                .seller(seller)
                .order(createOrderDto(invoice.getOrder()))
                .build();
    }

    public InvoiceDto toDto(Invoice invoice) {
        return InvoiceDto.builder()
                .id(invoice.getId())
                .number(invoice.getNumber())
                .createdAt(invoice.getCreatedAt())
                .buyer(new CompanyDto(invoice.getBuyerId()))
                .seller(new CompanyDto(invoice.getSellerId()))
                .order(createOrderDto(invoice.getOrder()))
                .build();
    }

    private OrderDto createOrderDto(Order order) {
        return OrderDto.builder()
                .netPriceTotal(order.getNetPriceTotal())
                .vatTotal(order.getVatTotal())
                .grossPriceTotal(order.getGrossPriceTotal())
                .orderDetails(createOrderDetailsDto(order.getOrderDetails()))
                .build();
    }

    private List<OrderDetailsDto> createOrderDetailsDto(List<OrderDetails> orderDetails) {
        return orderDetails.stream()
                .map(this::createOrderDetailsDto)
                .collect(Collectors.toList());
    }

    private OrderDetailsDto createOrderDetailsDto(OrderDetails orderDetails) {
        return OrderDetailsDto.builder()
                .positionNumber(orderDetails.getPositionNumber())
                .product(createProductDto(orderDetails.getProduct()))
                .quantity(orderDetails.getQuantity())
                .netPriceTotal(orderDetails.getNetTotal())
                .vatTotal(orderDetails.getVatTotal())
                .grossPriceTotal(orderDetails.getGrossPriceTotal())
                .build();
    }

    private ProductDto createProductDto(Product product) {
        return ProductDto.builder()
                .productName(product.getName())
                .netPrice(product.getNetPrice())
                .unitOfMeasure(product.getUnitOfMeasure())
                .vat(product.getVat().getRate())
                .grossPrice(product.getGrossPrice())
                .build();
    }
}
