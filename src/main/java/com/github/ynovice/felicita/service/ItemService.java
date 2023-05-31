package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.dto.request.ModifyItemRequestDto;
import com.github.ynovice.felicita.model.dto.request.ItemFilterParamsDto;
import com.github.ynovice.felicita.model.entity.Item;
import com.github.ynovice.felicita.model.entity.Reserve;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ItemService {

    Item createItem(@NonNull ModifyItemRequestDto modifyItemRequestDto);

    Item updateItem(Long id, @NonNull ModifyItemRequestDto requestDto);

    void updateItemQuantitiesAfterReserve(@NonNull Reserve reserve);

    Optional<Item> getById(@NonNull Long id);

    Page<Item> getByFilters(int page, ItemFilterParamsDto filterParams);

    void deleteById(@NonNull Long id);
}
