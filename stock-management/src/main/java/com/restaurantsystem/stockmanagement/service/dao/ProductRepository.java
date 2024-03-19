package com.restaurantsystem.stockmanagement.service.dao;

import com.restaurantsystem.stockmanagement.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
