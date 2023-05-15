package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.exception.InvalidEntityException;
import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.exception.InvalidEntityDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<InvalidEntityDto> handleInvalidEntityException(InvalidEntityException e) {

        return ResponseEntity
                .badRequest()
                .body(InvalidEntityDto.fromEntity(e.getBindingResult()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundException> handleNotFoundException(NotFoundException ignoredE) {
        return ResponseEntity.notFound().build();
    }
}
