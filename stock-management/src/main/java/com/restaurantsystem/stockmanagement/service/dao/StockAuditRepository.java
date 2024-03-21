package com.restaurantsystem.stockmanagement.service.dao;

import com.restaurantsystem.stockmanagement.entity.StockAudit;
import com.restaurantsystem.stockmanagement.entity.StockAuditType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface StockAuditRepository extends MongoRepository<StockAudit, String> {

    boolean existsByTypeAndDateGreaterThan(StockAuditType type, LocalDateTime date);

    Optional<StockAudit> findTopByStockAuditTypeAndOrderByDateDesc(StockAuditType type);
}
