package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Reserve;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ReserveShortInfoDto {

    protected static final String DATE_TIME_PRESENTATION_FORMAT = "dd.MM.YYYY в HH:mm";

    private Long id;
    private Integer totalPrice;
    private Integer totalItems;
    private ZonedDateTime createdAt;
    private String createdAtPresentation;
    private UserDto owner;

    public static ReserveShortInfoDto fromEntity(Reserve reserve) {

        ReserveShortInfoDto dto = new ReserveShortInfoDto();
        dto.setId(reserve.getId());
        dto.setTotalPrice(reserve.getTotalPrice());
        dto.setTotalItems(reserve.getTotalItems());
        dto.setCreatedAt(reserve.getCreatedAt());
        dto.setCreatedAtPresentation(
                reserve.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_TIME_PRESENTATION_FORMAT))
        );
        dto.setOwner(UserDto.fromEntity(reserve.getUser()));

        return dto;
    }
}
