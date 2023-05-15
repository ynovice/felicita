package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.MaterialDto;
import com.github.ynovice.felicita.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/material")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<MaterialDto>> getAll() {
        return ResponseEntity.ok(
                materialService.getAll()
                        .stream()
                        .map(MaterialDto::fromEntity)
                        .toList()
        );
    }
}
