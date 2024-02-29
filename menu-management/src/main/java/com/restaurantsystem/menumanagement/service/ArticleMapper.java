package com.restaurantsystem.menumanagement.service;

import com.restaurantsystem.menumanagement.entity.Article;
import com.restaurantsystem.menumanagement.web.dto.ArticleDto;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {
    public ArticleDto toDto(Article article) {
        return ArticleDto.builder()
                .name(article.getName())
                .shortName(article.getShortName())
                .category(article.getCategory())
                .vat(article.getVat().getRate())
                .grossPrice(article.getGrossPrice())
                .active(article.isActive())
                .build();
    }
}
