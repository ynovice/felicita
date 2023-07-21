package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.ArticleShortInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ArticleShortInfoDto {

    private static final String DATE_TIME_PRESENTATION_FORMAT = "dd.MM.YYYY в HH:mm";

    private Long id;
    private String createdAtPresentation;
    private Long previewId;
    private String name;
    private String author;

    public static ArticleShortInfoDto fromEntity(ArticleShortInfo articleShortInfo) {

        ArticleShortInfoDto dto = new ArticleShortInfoDto();
        dto.setId(articleShortInfo.getId());
        dto.setCreatedAtPresentation(
                articleShortInfo.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_TIME_PRESENTATION_FORMAT))
        );
        dto.setPreviewId(articleShortInfo.getPreviewId());
        dto.setName(articleShortInfo.getName());
        dto.setAuthor(articleShortInfo.getAuthor());

        return dto;
    }
}
