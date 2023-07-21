package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Article;
import com.github.ynovice.felicita.model.entity.ArticleShortInfo;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface Articles {

    void create(Article article);

    void update(Article article);

    List<ArticleShortInfo> findAll(Sort sort);

    Optional<Article> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);
}
