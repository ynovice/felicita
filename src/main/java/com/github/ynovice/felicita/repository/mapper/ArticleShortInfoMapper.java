package com.github.ynovice.felicita.repository.mapper;

import com.github.ynovice.felicita.model.entity.ArticleShortInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;

@Component
public class ArticleShortInfoMapper implements RowMapper<ArticleShortInfo> {

    @Override
    public ArticleShortInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

        ArticleShortInfo articleShortInfo = new ArticleShortInfo();
        articleShortInfo.setId(rs.getLong("id"));
        articleShortInfo.setCreatedAt(
                rs.getTimestamp("created_at")
                        .toInstant()
                        .atZone(TimeZone.getDefault().toZoneId())
        );
        articleShortInfo.setPreviewId(rs.getLong("preview_id"));
        articleShortInfo.setAuthor(rs.getString("author"));
        articleShortInfo.setName(rs.getString("name"));

        return articleShortInfo;
    }
}
