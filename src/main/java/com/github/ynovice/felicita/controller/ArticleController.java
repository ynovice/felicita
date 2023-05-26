package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ArticleDto;
import com.github.ynovice.felicita.model.dto.request.CreateArticleDto;
import com.github.ynovice.felicita.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ArticleDto> create(@RequestBody CreateArticleDto dto,
                                             @AuthenticationPrincipal OAuth2User principal) {

        return ResponseEntity.ok(
                ArticleDto.fromEntity(articleService.create(dto.getName(), dto.getContent(), principal))
        );
    }
}
