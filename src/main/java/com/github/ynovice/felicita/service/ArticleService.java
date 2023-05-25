package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Article;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ArticleService {

    Article create(String content, OAuth2User authorPrincipal);

    Article getById(Long id);
}
