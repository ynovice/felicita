package com.github.ynovice.felicita.repository.mapper;

import com.github.ynovice.felicita.model.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class ArticleRowMapper implements RowMapper<Article> {

    @Override
    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {

        Article article = new Article();
        article.setId(rs.getLong("id"));
        article.setCreatedAt(
                rs.getTimestamp("created_at")
                        .toInstant()
                        .atZone(TimeZone.getDefault().toZoneId())
        );
        article.setPreviewId(rs.getLong("preview_id"));
        article.setAuthor(rs.getString("author"));
        article.setName(rs.getString("name"));
        article.setContent(rs.getString("content"));

        return article;
    }
}
