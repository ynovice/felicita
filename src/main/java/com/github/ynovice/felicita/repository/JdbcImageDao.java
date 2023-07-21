package com.github.ynovice.felicita.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcImageDao implements Images {

    private final NamedParameterJdbcTemplate template;

    @Override
    public boolean existsById(Long id) {

        String sql = "SELECT EXISTS(SELECT 1 FROM images WHERE id = :id)";

        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        return Boolean.TRUE.equals(template.queryForObject(sql, parameterSource, Boolean.class));
    }
}
