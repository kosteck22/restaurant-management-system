package com.invoicesystem.companyservice.service;

import com.invoicesystem.companyservice.entity.Company;

import java.util.Optional;

public interface CompanyProvider {
    Optional<Company> findCompanyByNip(String nip);
}
