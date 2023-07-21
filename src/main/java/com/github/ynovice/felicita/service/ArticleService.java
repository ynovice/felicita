package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.dto.request.CreateArticleRequestDto;
import com.github.ynovice.felicita.model.dto.request.UpdateArticleDto;
import com.github.ynovice.felicita.model.entity.Article;
import com.github.ynovice.felicita.model.entity.ArticleShortInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface ArticleService {

    Article create(CreateArticleRequestDto dto, OAuth2User authorPrincipal);

    List<ArticleShortInfo> getAll();

    Article update(UpdateArticleDto dto);

    Article getById(Long id);

    void deleteById(Long id);
}
