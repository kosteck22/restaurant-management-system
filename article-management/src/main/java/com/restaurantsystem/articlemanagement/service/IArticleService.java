package com.restaurantsystem.articlemanagement.service;

import com.restaurantsystem.articlemanagement.entity.Article;
import com.restaurantsystem.articlemanagement.web.dto.ArticleRequest;

import java.util.List;

public interface IArticleService {
    List<Article> getActiveArticles();
    String save(ArticleRequest articleRequest);
}
