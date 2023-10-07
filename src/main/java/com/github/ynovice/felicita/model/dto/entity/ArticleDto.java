package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Article;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ArticleDto {

    private static final String DATE_TIME_PRESENTATION_FORMAT = "YYYY.MM.dd";

    private Long id;
    private String createdAtPresentation;
    private ImageDto preview;
    private String name;
    private String content;
    private String author;

    public static ArticleDto fromEntity(Article article) {

        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setCreatedAtPresentation(
                article.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_TIME_PRESENTATION_FORMAT))
        );
        dto.setPreview(
                ImageDto.fromEntity(article.getPreview())
        );
        dto.setName(article.getName());
        dto.setContent(article.getContent());
        dto.setAuthor(article.getAuthor());

        return dto;
    }
}
