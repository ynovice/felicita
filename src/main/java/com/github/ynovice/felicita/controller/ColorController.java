package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ColorDto;
import com.github.ynovice.felicita.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<List<ColorDto>> getAll() {
        return ResponseEntity.ok(
            colorService.getAll()
                    .stream()
                    .map(ColorDto::fromEntity)
                    .toList()
        );
    }
}
