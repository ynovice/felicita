package com.github.ynovice.felicita.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateSizeQuantityRequestDto {

    private Long sizeId;
    private Integer quantity;
}
