package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.exception.InvalidEntityException;
import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.request.CreateArticleRequestDto;
import com.github.ynovice.felicita.model.dto.request.UpdateArticleDto;
import com.github.ynovice.felicita.model.entity.Article;
import com.github.ynovice.felicita.model.entity.ArticleShortInfo;
import com.github.ynovice.felicita.model.entity.User;
import com.github.ynovice.felicita.repository.Articles;
import com.github.ynovice.felicita.service.ArticleService;
import com.github.ynovice.felicita.service.UserService;
import com.github.ynovice.felicita.validator.ArticleValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final Articles articles;

    private final UserService userService;

    private final ArticleValidator articleValidator;

    @Override
    public Article create(CreateArticleRequestDto dto, OAuth2User authorPrincipal) {

        User user = userService.getUser(authorPrincipal);

        Article article = new Article();
        article.setCreatedAt(ZonedDateTime.now());
        article.setAuthor(user.getUsername());
        article.setName(dto.getName());
        article.setContent(dto.getContent());
        article.setPreviewId(dto.getPreviewId());

        BindingResult validationResult = validate(article);
        if (validationResult.hasErrors()) throw new InvalidEntityException(validationResult);

        articles.create(article);
        return article;
    }

    @Override
    public List<ArticleShortInfo> getAll() {
        return articles.findAll(null);
    }

    @Override
    public Article update(UpdateArticleDto dto) {

        Article article = articles.findById(dto.getId())
                .orElseThrow(NotFoundException::new);

        article.setName(dto.getName());
        article.setContent(dto.getContent());
        article.setPreviewId(dto.getPreviewId());

        BindingResult validationResult = validate(article);
        if(validationResult.hasErrors()) throw new InvalidEntityException(validationResult);

        articles.update(article);
        return article;
    }

    @Override
    public Article getById(Long id) {
        return articles.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        articles.deleteById(id);
    }

    private BindingResult validate(@NonNull Article article) {
        DataBinder dataBinder = new DataBinder(article);
        dataBinder.addValidators(articleValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
