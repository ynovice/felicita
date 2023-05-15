package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Item;
import com.github.ynovice.felicita.model.dto.request.CreateItemRequestDto;
import lombok.NonNull;

import java.util.Optional;

public interface ItemService {

    Item createItem(@NonNull CreateItemRequestDto createItemRequestDto);

    Optional<Item> getById(@NonNull Long id);
}
