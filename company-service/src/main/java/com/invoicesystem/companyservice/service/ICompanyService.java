package com.invoicesystem.companyservice.service;

import com.invoicesystem.companyservice.web.CompanyDto;

import java.util.List;

public interface ICompanyService {
    CompanyDto getByNip(String nip);
    CompanyDto get(String id);
    String saveOrGetId(CompanyDto requestCompany);
    List<String> getIdsForCompanyName(String name);
}
