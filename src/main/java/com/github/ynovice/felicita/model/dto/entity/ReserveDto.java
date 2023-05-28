package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Reserve;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class ReserveDto {

    private Long id;
    private List<ReserveEntryDto> entries;
    private Integer totalPrice;
    private Integer totalItems;
    private ZonedDateTime createdAt;

    public static ReserveDto fromEntity(Reserve reserve) {

        ReserveDto dto = new ReserveDto();
        dto.setId(reserve.getId());
        dto.setEntries(
                reserve.getEntries()
                        .stream()
                        .map(ReserveEntryDto::fromEntity)
                        .toList()
        );
        dto.setTotalPrice(reserve.getTotalPrice());
        dto.setTotalItems(reserve.getTotalItems());
        dto.setCreatedAt(reserve.getCreatedAt());

        return dto;
    }
}
