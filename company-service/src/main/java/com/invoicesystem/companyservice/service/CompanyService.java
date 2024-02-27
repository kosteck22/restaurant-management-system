package com.invoicesystem.companyservice.service;

import com.invoicesystem.companyservice.*;
import com.invoicesystem.companyservice.entity.Company;
import com.invoicesystem.companyservice.exception.RequestValidationException;
import com.invoicesystem.companyservice.exception.ResourceNotFoundException;
import com.invoicesystem.companyservice.web.CompanyDto;
import com.invoicesystem.companyservice.web.CompanyIdProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService implements ICompanyService {
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final CompanyProvider companyProvider;
    private final CompanyValidator companyValidator;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper, CompanyProvider companyProvider, CompanyValidator companyValidator) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.companyProvider = companyProvider;
        this.companyValidator = companyValidator;
    }

    @Override
    public CompanyDto get(String id) {
        return companyRepository.findById(id)
                .map(companyMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id %s not found".formatted(id)));
    }

    @Override
    public String saveOrGetId(CompanyDto requestCompany) {
        Company company = companyMapper.toEntity(requestCompany);
        validateCompany(company);
        return companyRepository.findByNip(company.getNip())
                .map(Company::getId)
                .orElseGet(() -> companyRepository.save(company).getId());
    }

    @Override
    public CompanyDto getByNip(String nip) {
        validateNip(nip);
        return companyRepository.findByNip(nip)
                .map(companyMapper)
                .orElseGet(() -> fetchAndSaveCompanyFromExternalApi(nip));
    }

    @Override
    public List<String> getIdsForCompanyName(String name) {
        return companyRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(CompanyIdProjection::getId)
                .collect(Collectors.toList());
    }

    private CompanyDto fetchAndSaveCompanyFromExternalApi(String nip) {
        Company company = companyProvider.findCompanyByNip(nip.replace("-", ""))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "There is no company for given nip. Please send your custom Company information"));
        validateCompany(company);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.apply(savedCompany);
    }

    private void validateCompany(Company company) {
        CompanyValidationResult validationResult = companyValidator.validate(company);
        if (validationResult.isNotValid()) {
            logger.info("Validation errors: {}", String.join(", ", validationResult.errorMessages()));
            throw new RequestValidationException("Incorrect values: %s".formatted(String.join(", ", validationResult.errorMessages())));
        }
    }

    private void validateNip(String nip) {
        if (isNipInvalid(nip)) {
            throw new RequestValidationException(
                    String.format("Tax ID number (NIP) is not valid: %s", nip));
        }
    }

    private boolean isNipInvalid(String nip) {
        if (isNullOrEmpty(nip)) {
            return true;
        }

        String normalizedNip = nip.replace("-", "");
        return !normalizedNip.matches("^\\d{10}$");
    }

    private boolean isNullOrEmpty(String field) {
        return (field == null || field.trim().isEmpty());
    }
}
