package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ArticleDto;
import com.github.ynovice.felicita.model.dto.request.CreateArticleDto;
import com.github.ynovice.felicita.model.dto.request.UpdateArticleDto;
import com.github.ynovice.felicita.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ArticleDto.fromEntity(articleService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAll() {
        return ResponseEntity.ok(
                articleService.getAll()
                        .stream()
                        .map(ArticleDto::fromEntity)
                        .toList()
        );
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    public ResponseEntity<ArticleDto> update(@RequestBody UpdateArticleDto dto) {
        return ResponseEntity.ok(
                ArticleDto.fromEntity(articleService.update(dto))
        );
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<ArticleDto> create(@RequestBody CreateArticleDto dto,
                                             @AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                ArticleDto.fromEntity(articleService.create(dto, principal))
        );
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        articleService.deleteById(id);
    }
}
