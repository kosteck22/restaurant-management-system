package com.restaurantsystem.salesmanagement.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Builder
public class Sale {
    @Id
    private String id;
    private LocalDateTime date;
    private List<SoldItem> items;
    private Integer saleDiscount;
    private BigDecimal netPrice;
    private BigDecimal grossPrice;
    private BigDecimal vatTotal;
    private List<VatDetail> vatDetails;
    private List<PaymentDetail> paymentDetails;
}
