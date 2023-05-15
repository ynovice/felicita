package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ItemDto;
import com.github.ynovice.felicita.model.dto.request.CreateItemRequestDto;
import com.github.ynovice.felicita.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestBody CreateItemRequestDto requestDto) {
        return ResponseEntity.ok(
            ItemDto.fromEntity(itemService.createItem(requestDto))
        );
    }
}
