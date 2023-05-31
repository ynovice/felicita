package com.github.ynovice.felicita.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModifyItemRequestDto {

    private String name;
    private String description;

    private List<Long> imagesIds;
    private List<Long> categoriesIds;

    private List<Long> materialsIds;
    private List<Long> colorsIds;
    private Boolean hasPrint;

    private Integer price;

    private List<CreateSizeQuantityRequestDto> sizesQuantities;
}
