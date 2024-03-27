package com.restaurantsystem.stockmanagement.service.dao;

import com.restaurantsystem.stockmanagement.entity.StockAudit;
import com.restaurantsystem.stockmanagement.entity.StockAuditType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StockAuditRepository extends MongoRepository<StockAudit, String> {
    boolean existsByStockAuditTypeAndDateGreaterThan(StockAuditType stockAuditType, LocalDateTime date);
    Optional<StockAudit> findTopByStockAuditTypeOrderByDateDesc(StockAuditType stockAuditType);
}
