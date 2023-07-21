package com.github.ynovice.felicita.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ArticleShortInfo {

    @Id
    protected Long id;
    protected ZonedDateTime createdAt;
    protected Long previewId;
    protected String author;
    protected String name;
}
