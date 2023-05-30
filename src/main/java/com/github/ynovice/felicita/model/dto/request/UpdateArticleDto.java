package com.github.ynovice.felicita.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateArticleDto {

    private Long id;
    private String name;
    private Long previewId;
    private String content;
}
