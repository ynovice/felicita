package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private Long id;

    public static ImageDto fromEntity(Image image) {

        if(image == null) return null;

        ImageDto dto = new ImageDto();
        dto.setId(image.getId());

        return dto;
    }
}
