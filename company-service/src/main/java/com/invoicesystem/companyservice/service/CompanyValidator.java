package com.invoicesystem.companyservice.service;

import com.invoicesystem.companyservice.entity.Company;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyValidator {

    public CompanyValidationResult validate(Company company) {
        List<String> errors = new ArrayList<>();

        if (isNullOrEmpty(company.getName())) {
            errors.add("Company name is missing.");
        }
        if (isNullOrEmpty(company.getNip())) {
            errors.add("Tax ID (NIP) is missing.");
        }
        if (!isNullOrEmpty(company.getNip()) && isNipIncorrectLength(company.getNip())) {
            errors.add("Tax ID (NIP) must be 10 digits long.");
        }
        /*  if (isNullOrEmpty(company.getRegon())) {
            errors.add("REGON is missing.");
        }*/
        if (!isNullOrEmpty(company.getRegon()) && isRegonIncorrectLength(company.getRegon())) {
            errors.add("REGON must be 8 digits long.");
        }
        if (isNullOrEmpty(company.getCity())) {
            errors.add("City is missing.");
        }
        if (isNullOrEmpty(company.getStreet())) {
            errors.add("Street is missing.");
        }
        if (isNullOrEmpty(company.getPostalCode())) {
            errors.add("Postal code is missing.");
        }
        if (!isNullOrEmpty(company.getPostalCode()) && isPostalCodeIncorrectFormat(company.getPostalCode())) {
            errors.add("Postal code format is not correct must be **-*** where * is digit");
        }

        return new CompanyValidationResult(errors.isEmpty(), errors);
    }

    private boolean isPostalCodeIncorrectFormat(String postalCode) {
        return postalCode.matches("^\\d{2}-\\d{3}$");
    }

    private boolean isRegonIncorrectLength(String regon) {
        return regon.matches("^\\d{8}$");
    }

    private boolean isNipIncorrectLength(String nip) {
        String normalizedNip = nip.replace("-", "");
        return !normalizedNip.matches("^\\d{10}$");
    }

    private boolean isNullOrEmpty(String field) {
        return (field == null || field.trim().isEmpty());
    }
}
