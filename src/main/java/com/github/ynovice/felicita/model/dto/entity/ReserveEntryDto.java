package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.ReserveEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReserveEntryDto {

    private Long id;
    private ItemDto item;
    private Integer pricePerItem;
    private List<SizeQuantityDto> sizesQuantities;

    public static ReserveEntryDto fromEntity(ReserveEntry reserveEntry) {

        ReserveEntryDto dto = new ReserveEntryDto();
        dto.setId(reserveEntry.getId());
        dto.setItem(ItemDto.fromEntity(reserveEntry.getItem()));
        dto.setPricePerItem(reserveEntry.getPricePerItem());

        dto.setSizesQuantities(
                reserveEntry.getSizesQuantities()
                        .stream()
                        .map(SizeQuantityDto::fromEntity)
                        .toList()
        );

        return dto;
    }
}
