package com.restaurantsystem.salesmanagement.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Builder
@Getter
public class Sale {
    @Id
    private String id;
    private LocalDateTime date;
    private List<SaleItem> items;
    private BigDecimal grossPrice;
    private BigDecimal netPrice;
    private BigDecimal vatTotal;
    private List<VatDetail> vatDetails;
    private List<PaymentDetail> paymentDetails;
}
