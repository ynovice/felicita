package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Material;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialDto {

    private Long id;
    private String name;

    public static MaterialDto fromEntity(Material material) {

        if(material == null) return null;

        MaterialDto dto = new MaterialDto();
        dto.setId(material.getId());
        dto.setName(material.getName());

        return dto;
    }
}
