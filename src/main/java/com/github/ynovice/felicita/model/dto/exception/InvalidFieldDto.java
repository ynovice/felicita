package com.github.ynovice.felicita.model.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@AllArgsConstructor
@Getter
@Setter
public class InvalidFieldDto {

    private String errorCode;
    private String fieldName;
    private String errorMessage;

    public static InvalidFieldDto fromEntity(FieldError fieldError) {

        return new InvalidFieldDto(
                fieldError.getCode(),
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }
}
