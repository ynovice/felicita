package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Item;
import com.github.ynovice.felicita.model.dto.request.CreateItemRequestDto;

public interface ItemService {

    Item createItem(CreateItemRequestDto createItemRequestDto);
}
