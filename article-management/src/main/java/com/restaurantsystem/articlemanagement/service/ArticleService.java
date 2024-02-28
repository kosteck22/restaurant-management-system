package com.restaurantsystem.articlemanagement.service;

import com.restaurantsystem.articlemanagement.entity.Article;
import com.restaurantsystem.articlemanagement.entity.VatRate;
import com.restaurantsystem.articlemanagement.exception.DuplicateResourceException;
import com.restaurantsystem.articlemanagement.exception.ResourceNotFoundException;
import com.restaurantsystem.articlemanagement.service.dao.ArticleRepository;
import com.restaurantsystem.articlemanagement.web.dto.ArticleDto;
import com.restaurantsystem.articlemanagement.web.dto.ArticleRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public List<ArticleDto> getActiveArticles() {
        return articleRepository.findByActiveTrue().stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDto getById(String id) {
        return articleRepository.findById(id)
                .map(articleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Article with id [%s] not found".formatted(id)));
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
