package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {}
