package com.invoicesystem.companyservice;

import com.invoicesystem.companyservice.entity.Company;
import com.invoicesystem.companyservice.web.CompanyDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CompanyMapper implements Function<Company, CompanyDto> {

    @Override
    public CompanyDto apply(Company company) {
        return CompanyDto.builder()
                .name(company.getName())
                .nip(company.getNip())
                .regon(company.getRegon())
                .city(company.getCity())
                .street(company.getStreet())
                .postalCode(company.getPostalCode())
                .build();
    }

    public Company toEntity(CompanyDto dto) {
        return Company.builder()
                .name(dto.name())
                .nip(dto.nip())
                .regon(dto.regon())
                .street(dto.street())
                .city(dto.city())
                .postalCode(dto.postalCode())
                .build();
    }
}
