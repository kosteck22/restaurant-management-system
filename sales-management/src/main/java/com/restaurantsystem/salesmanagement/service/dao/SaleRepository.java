package com.restaurantsystem.salesmanagement.service.dao;

import com.restaurantsystem.salesmanagement.entity.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaleRepository extends MongoRepository<Sale, String> {
}
