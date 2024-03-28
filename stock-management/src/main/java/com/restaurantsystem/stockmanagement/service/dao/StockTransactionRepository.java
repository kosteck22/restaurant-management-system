package com.restaurantsystem.stockmanagement.service.dao;

import com.restaurantsystem.stockmanagement.entity.StockTransaction;
import com.restaurantsystem.stockmanagement.entity.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StockTransactionRepository extends MongoRepository<StockTransaction, String> {
    List<StockTransaction> findByDateGreaterThanAndTransactionType(LocalDateTime date, TransactionType transactionType);
    List<StockTransaction> findByTransactionType(TransactionType transactionType);
}
