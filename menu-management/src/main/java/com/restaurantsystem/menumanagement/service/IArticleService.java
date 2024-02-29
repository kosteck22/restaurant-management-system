package com.restaurantsystem.menumanagement.service;

import com.restaurantsystem.menumanagement.web.dto.ArticleDto;
import com.restaurantsystem.menumanagement.web.dto.ArticleRequest;

import java.util.List;

public interface IArticleService {
    List<ArticleDto> getActiveArticles();
    String save(ArticleRequest articleRequest);
    ArticleDto getById(String id);
}