package com.github.ynovice.felicita.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateArticleDto {

    private String name;
    private String content;
}
