package com.invoicesystem.companyservice.service;

import com.invoicesystem.companyservice.entity.Company;
import com.invoicesystem.companyservice.web.CompanyIdProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    Optional<Company> findByNip(String nip);
    List<CompanyIdProjection> findByNameContainingIgnoreCase(String name);
}
