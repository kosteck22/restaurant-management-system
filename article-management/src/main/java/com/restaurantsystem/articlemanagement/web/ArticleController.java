package com.restaurantsystem.articlemanagement.web;

import com.restaurantsystem.articlemanagement.entity.Article;
import com.restaurantsystem.articlemanagement.service.IArticleService;
import com.restaurantsystem.articlemanagement.web.dto.ArticleRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final IArticleService articleService;

    public ArticleController(IArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<Article>> getArticles() {
        List<Article> articles = articleService.getActiveArticles();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(articles);
    }

    @PostMapping
    public ResponseEntity<String> save(@Valid @RequestBody ArticleRequest articleRequest) {
        String id = articleService.save(articleRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(id);
    }
}
