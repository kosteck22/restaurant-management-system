package com.restaurantsystem.menumanagement.service.dao;

import com.restaurantsystem.menumanagement.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
    List<Article> findByActiveTrue();
    Optional<Article> findByName(String name);
    Optional<Article> findByShortName(String shortName);
}
