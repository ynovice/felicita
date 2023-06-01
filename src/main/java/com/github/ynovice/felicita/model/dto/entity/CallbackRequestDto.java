package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.CallbackRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CallbackRequestDto {

    private static final String DATE_TIME_PRESENTATION_FORMAT = "dd.MM.YYYY в HH:mm";

    private Long id;
    private String name;
    private String phone;
    private ZonedDateTime createdAt;
    private String createdAtPresentation;

    public static CallbackRequestDto fromEntity(CallbackRequest callbackRequest) {

        CallbackRequestDto dto = new CallbackRequestDto();
        dto.setId(callbackRequest.getId());
        dto.setName(callbackRequest.getName());
        dto.setPhone(callbackRequest.getPhone());
        dto.setCreatedAt(callbackRequest.getCreatedAt());
        dto.setCreatedAtPresentation(
                callbackRequest.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_TIME_PRESENTATION_FORMAT))
        );

        return dto;
    }
}
