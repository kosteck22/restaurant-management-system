package com.restaurantsystem.articlemanagement.service;

import com.restaurantsystem.articlemanagement.entity.Article;
import com.restaurantsystem.articlemanagement.entity.VatRate;
import com.restaurantsystem.articlemanagement.exception.DuplicateResourceException;
import com.restaurantsystem.articlemanagement.service.dao.ArticleRepository;
import com.restaurantsystem.articlemanagement.web.dto.ArticleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getActiveArticles() {
        return articleRepository.findByActiveTrue();
    }

    @Override
    public String save(ArticleRequest articleRequest) {
        throwIfNameAlreadyExist(articleRequest.name());
        throwIfShortNameAlreadyExist(articleRequest.shortName());
        VatRate vatRate = VatRate.valueOf(articleRequest.vat());

        Article article = Article.builder()
                .name(articleRequest.name())
                .shortName(articleRequest.shortName())
                .category(articleRequest.category())
                .vat(vatRate)
                .grossPrice(articleRequest.grossPrice())
                .active(articleRequest.active())
                .build();

        return articleRepository.save(article).getId();
    }

    private void throwIfShortNameAlreadyExist(String shortName) {
        articleRepository.findByShortName(shortName)
                .ifPresent(a -> {
                    throw new DuplicateResourceException("Article with shortName [%s] already exist".formatted(shortName));
                });
    }

    private void throwIfNameAlreadyExist(String name) {
        articleRepository.findByName(name)
                .ifPresent(a -> {
                    throw new DuplicateResourceException("Article with name [%s] already exist".formatted(name));
                });
    }
}
