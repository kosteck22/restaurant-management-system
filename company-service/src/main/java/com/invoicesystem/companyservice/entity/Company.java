package com.invoicesystem.companyservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "companies")
public class Company {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Indexed(unique = true)
    private String nip;

    @Indexed(unique = true)
    private String regon;

    private String street;
    private String city;
    private String postalCode;
}
