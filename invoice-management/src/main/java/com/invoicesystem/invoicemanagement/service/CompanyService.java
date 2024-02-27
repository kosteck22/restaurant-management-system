package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.exception.RequestValidationException;
import com.invoicesystem.invoicemanagement.web.client.CompanyClient;
import com.invoicesystem.invoicemanagement.web.dto.CompanyDto;
import com.invoicesystem.invoicemanagement.web.dto.request.CompanyRequest;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyClient companyClient;
    private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    public CompanyService(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    public List<String> getCompaniesIdsForName(String companyName) {
        logger.info("Getting Companies Ids for given name: {}", companyName);
        try {
            return companyClient.getIdsForCompanyName(companyName).getBody();
        } catch (FeignException e) {
            throw new RequestValidationException("Company with given name not found: " + companyName);
        }
    }

    public String saveOrGetCompanyId(CompanyRequest company, String companyRole) {
        logger.info("Fetching or saving company ID for {}: {}", companyRole, company);
        try {
            return companyClient.saveOrGetId(company).getBody();
        } catch (FeignException e) {
            throw new RequestValidationException(companyRole + " company processing failed, msg: " + e.getMessage());
        }
    }

    public CompanyDto getCompanyById(String companyId) {
        logger.info("Fetching company by ID: {}", companyId);
        try {
            return companyClient.get(companyId).getBody();
        } catch (FeignException e) {
            logger.error("Error from company-service for id: {}", companyId, e);
            throw new InternalError("We have a problem with connection between our services. Please try later.");
        }
    }
}
