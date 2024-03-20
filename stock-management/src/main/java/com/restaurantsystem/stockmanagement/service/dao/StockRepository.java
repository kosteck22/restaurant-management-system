package com.restaurantsystem.stockmanagement.service.dao;

import com.restaurantsystem.stockmanagement.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StockRepository extends MongoRepository<Stock, String> {
    Optional<Stock> findByProductId(String productId);
}
