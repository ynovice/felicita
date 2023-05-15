package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.entity.ItemDto;
import com.github.ynovice.felicita.model.dto.request.CreateItemRequestDto;
import com.github.ynovice.felicita.model.entity.Item;
import com.github.ynovice.felicita.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        itemService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Long id) {

        Item item = itemService.getById(id).orElseThrow(NotFoundException::new);

        return ResponseEntity.ok(ItemDto.fromEntity(item));
    }
}
