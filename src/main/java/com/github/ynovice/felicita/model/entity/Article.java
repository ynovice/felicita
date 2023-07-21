package com.github.ynovice.felicita.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Article extends ArticleShortInfo {

    private String content;

    public Article(ArticleShortInfo articleShortInfo) {

        id = articleShortInfo.getId();
        createdAt = articleShortInfo.getCreatedAt();
        previewId = articleShortInfo.getPreviewId();
        author = articleShortInfo.getAuthor();
        name = articleShortInfo.getName();
    }
}
