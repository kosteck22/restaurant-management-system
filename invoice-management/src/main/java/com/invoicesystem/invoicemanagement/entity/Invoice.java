package com.invoicesystem.invoicemanagement.entity;

import com.invoicesystem.invoicemanagement.entity.Order.Order;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "invoices")
@Data
@Builder
public class Invoice {
    @Id
    private String id;

    @Indexed(unique = true)
    private String number;

    private LocalDate createdAt;
    private String sellerId;
    private String buyerId;
    private Order order;
}
