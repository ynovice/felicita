package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.Item;
import com.github.ynovice.felicita.model.request.CreateItemRequestDto;

public interface ItemService {

    Item createItem(CreateItemRequestDto createItemRequestDto);
}
