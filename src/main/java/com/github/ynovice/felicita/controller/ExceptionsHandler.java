package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.exception.BadRequestException;
import com.github.ynovice.felicita.exception.InvalidEntityException;
import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.exception.ExceptionDto;
import com.github.ynovice.felicita.model.dto.exception.InvalidEntityDto;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ExceptionDto> handleNotFoundException(NotFoundException e) {
        ExceptionDto exceptionDto = new ExceptionDto("notFound", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDto> handleBadRequestException(BadRequestException e) {
        ExceptionDto exceptionDto = new ExceptionDto("badRequest", e.getMessage());
        return ResponseEntity.badRequest().body(exceptionDto);
    }
}
