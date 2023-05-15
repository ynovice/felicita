package com.github.ynovice.felicita.model.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.util.List;


@AllArgsConstructor
@Getter
@Setter
public class InvalidEntityDto {

    private List<InvalidFieldDto> invalidFields;

    public static InvalidEntityDto fromEntity(BindingResult bindingResult) {

        return new InvalidEntityDto(
                bindingResult.getFieldErrors()
                        .stream()
                        .map(InvalidFieldDto::fromEntity)
                        .toList()
        );
    }
}
