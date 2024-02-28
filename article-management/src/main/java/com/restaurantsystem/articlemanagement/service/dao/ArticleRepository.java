package com.restaurantsystem.articlemanagement.service.dao;

import com.restaurantsystem.articlemanagement.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
    List<Article> findByActiveTrue();
}
