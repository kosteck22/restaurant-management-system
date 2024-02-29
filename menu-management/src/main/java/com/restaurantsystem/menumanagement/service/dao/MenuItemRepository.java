package com.restaurantsystem.menumanagement.service.dao;

import com.restaurantsystem.menumanagement.entity.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    List<MenuItem> findByActiveTrue();
    Optional<MenuItem> findByName(String name);
    Optional<MenuItem> findByShortName(String shortName);
}
