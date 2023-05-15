package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.CategoryDto;
import com.github.ynovice.felicita.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getTrees() {
        return ResponseEntity.ok(
            categoryService.getAllRootcategories()
                    .stream()
                    .map(CategoryDto::fromEntity)
                    .toList()
        );
    }
}
