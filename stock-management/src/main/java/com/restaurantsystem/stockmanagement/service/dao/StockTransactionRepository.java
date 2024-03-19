package com.restaurantsystem.stockmanagement.service.dao;

import com.restaurantsystem.stockmanagement.entity.StockTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockTransactionRepository extends MongoRepository<StockTransaction, String> {
}
