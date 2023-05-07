package com.github.ynovice.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateSizeQuantityRequestDto {

    private Long sizeId;
    private Integer quantity;
}
