package com.restaurantsystem.articlemanagement.service;

import com.restaurantsystem.articlemanagement.web.dto.ArticleDto;
import com.restaurantsystem.articlemanagement.web.dto.ArticleRequest;

import java.util.List;

public interface IArticleService {
    List<ArticleDto> getActiveArticles();
    String save(ArticleRequest articleRequest);
    ArticleDto getById(String id);
}
