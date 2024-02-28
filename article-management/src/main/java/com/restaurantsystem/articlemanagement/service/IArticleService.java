package com.restaurantsystem.articlemanagement.service;

import com.restaurantsystem.articlemanagement.entity.Article;
import java.util.List;

public interface IArticleService {
    List<Article> getActiveArticles();
}
