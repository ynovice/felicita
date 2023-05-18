package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.CartEntry;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartEntryDto {

    private Long userId;
    private Long itemId;
    private List<SizeQuantityDto> sizesQuantities;

    public static CartEntryDto fromEntity(@NonNull CartEntry cartEntry) {

        CartEntryDto dto = new CartEntryDto();
        dto.setUserId(cartEntry.getUserId());
        dto.setItemId(cartEntry.getItemId());
        dto.setSizesQuantities(
                cartEntry.getSizesQuantities()
                        .stream()
                        .map(SizeQuantityDto::fromEntity)
                        .toList()
        );

        return dto;
    }
}
