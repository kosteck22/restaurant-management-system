package com.invoicesystem.invoicemanagement.service.dao;

import com.invoicesystem.invoicemanagement.entity.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String>, CustomInvoiceRepository {
    Optional<Invoice> findByNumber(String number);
}
