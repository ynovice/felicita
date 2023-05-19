package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.dto.request.ItemFilterParamsDto;
import com.github.ynovice.felicita.model.entity.Item;
import com.github.ynovice.felicita.model.dto.request.CreateItemRequestDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ItemService {

    Item createItem(@NonNull CreateItemRequestDto createItemRequestDto);

    Optional<Item> getById(@NonNull Long id);

    Page<Item> getByFilters(int page, ItemFilterParamsDto filterParams);

    void deleteById(@NonNull Long id);
}
