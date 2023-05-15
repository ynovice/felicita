package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorDto {

    private Long id;
    private String name;

    public static ColorDto fromEntity(Color color) {

        if(color == null) return null;

        ColorDto dto = new ColorDto();
        dto.setId(color.getId());
        dto.setName(color.getName());

        return dto;
    }
}
