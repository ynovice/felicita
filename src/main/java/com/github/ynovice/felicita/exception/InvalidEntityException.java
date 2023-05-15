package com.github.ynovice.felicita.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor
@Getter
@Setter
public class InvalidEntityException extends RuntimeException {

    private final BindingResult bindingResult;
}
