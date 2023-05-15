package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.SizeDto;
import com.github.ynovice.felicita.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/size")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @GetMapping
    public ResponseEntity<List<SizeDto>> getAll() {
        return ResponseEntity.ok(
            sizeService.getAll()
                    .stream()
                    .map(SizeDto::fromEntity)
                    .toList()
        );
    }
}
