package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.SizeQuantity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeQuantityDto {

    private SizeDto size;
    private Integer quantity;

    public static SizeQuantityDto fromEntity(SizeQuantity sizeQuantity) {

        if(sizeQuantity == null) return null;

        SizeQuantityDto dto = new SizeQuantityDto();
        dto.setSize(
                SizeDto.fromEntity(sizeQuantity.getSize())
        );
        dto.setQuantity(sizeQuantity.getQuantity());

        return dto;
    }
}
