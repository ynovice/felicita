package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeDto {

    private Long id;
    private String name;

    public static SizeDto fromEntity(Size size) {

        if(size == null) return null;

        SizeDto dto = new SizeDto();
        dto.setId(size.getId());
        dto.setName(size.getName());

        return dto;
    }
}
