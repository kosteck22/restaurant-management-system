package com.invoiceSystem.invoiceExtractor.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Company {
    private String id;
    private String name;
    private String nip;
    private String regon;
    private String street;
    private String city;
    private String postalCode;
}
