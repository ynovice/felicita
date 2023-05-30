package com.github.ynovice.felicita.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateArticleRequestDto {

    private String name;
    private Long previewId;
    private String content;
}
