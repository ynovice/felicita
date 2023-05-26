package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.dto.request.CreateArticleDto;
import com.github.ynovice.felicita.model.dto.request.UpdateArticleDto;
import com.github.ynovice.felicita.model.entity.Article;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ArticleService {

    Article create(CreateArticleDto dto, OAuth2User authorPrincipal);

    Article update(UpdateArticleDto dto);

    Article getById(Long id);
}
