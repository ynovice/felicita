package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.entity.ItemDto;
import com.github.ynovice.felicita.model.dto.request.ModifyItemRequestDto;
import com.github.ynovice.felicita.model.dto.request.ItemFilterParamsDto;
import com.github.ynovice.felicita.model.dto.response.ItemsPageDto;
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
    public ResponseEntity<ItemDto> create(@RequestBody ModifyItemRequestDto requestDto) {
        return ResponseEntity.ok(
            ItemDto.fromEntity(itemService.createItem(requestDto))
        );
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateById(@PathVariable Long id,
                                              @RequestBody ModifyItemRequestDto requestDto) {
        return ResponseEntity.ok(
                ItemDto.fromEntity(itemService.updateItem(id, requestDto))
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

    @GetMapping
    public ResponseEntity<ItemsPageDto> getByFilters(@RequestParam(defaultValue = "0") int page,
                                                     ItemFilterParamsDto filterParams) {

        return ResponseEntity.ok(
                ItemsPageDto.fromEntity(itemService.getByFilters(page, filterParams))
        );
    }
}
