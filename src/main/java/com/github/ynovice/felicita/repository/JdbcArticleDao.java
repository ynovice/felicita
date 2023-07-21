package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Article;
import com.github.ynovice.felicita.model.entity.ArticleShortInfo;
import com.github.ynovice.felicita.repository.mapper.ArticleRowMapper;
import com.github.ynovice.felicita.repository.mapper.ArticleShortInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcArticleDao implements Articles {

    private final NamedParameterJdbcTemplate template;

    private final ArticleRowMapper articleRowMapper;
    private final ArticleShortInfoMapper articleShortInfoMapper;

    @Override
    public void create(Article article) {

        String sql = "INSERT INTO articles (author, name, content, preview_id) " +
                "VALUES (:author, :name, :content, :preview_id) RETURNING id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("author", article.getAuthor())
                .addValue("name", article.getName())
                .addValue("content", article.getContent())
                .addValue("preview_id", article.getPreviewId());

        Long id = template.queryForObject(sql, parameterSource, Long.class);
        article.setId(id);
    }

    @Override
    public void update(Article article) {

        String sql = "UPDATE articles SET name = :name, content = :content, preview_id = :preview_id WHERE id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", article.getName())
                .addValue("content", article.getContent())
                .addValue("preview_id", article.getPreviewId())
                .addValue("id", article.getId());

        template.update(sql, parameterSource);
    }

    @Override
    public List<ArticleShortInfo> findAll(Sort sort) {  // TODO apply sorting

        String sql = "SELECT a.id, a.author, a.name, a.content, a.created_at, a.preview_id, i.extension " +
                "FROM articles a " +
                "LEFT JOIN images i on a.preview_id = i.id";

        return template.query(sql, articleShortInfoMapper);
    }

    @Override
    public Optional<Article> findById(Long id) {

        if(!existsById(id))
            return Optional.empty();

        String sql = "SELECT a.id, a.author, a.name, a.content, a.created_at, a.preview_id, i.extension " +
                "FROM articles a " +
                "LEFT JOIN images i on a.preview_id = i.id " +
                "WHERE a.id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        return Optional.ofNullable(template.queryForObject(sql, parameterSource, articleRowMapper));
    }

    @Override
    public void deleteById(Long id) {

        String sql = "DELETE FROM articles WHERE id = :id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        template.update(sql, parameterSource);
    }

    @Override
    public boolean existsById(Long id) {

        String sql = "SELECT EXISTS(SELECT 1 FROM articles WHERE id = :id)";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        return Boolean.TRUE.equals(template.queryForObject(sql, parameterSource, Boolean.class));
    }
}
