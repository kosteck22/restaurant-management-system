package com.restaurantsystem.stockmanagement.service.dao;

import com.restaurantsystem.stockmanagement.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends MongoRepository<Stock, String> {
    Optional<Stock> findByProductId(String productId);
    List<Stock> findAllByProductIdIn(List<String> ids);
}
