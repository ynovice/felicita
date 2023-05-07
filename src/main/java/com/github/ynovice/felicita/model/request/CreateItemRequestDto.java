package com.github.ynovice.felicita.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateItemRequestDto {

    private String name;
    private String description;

    private List<Long> imagesIds;
    private Set<Long> categoriesIds;

    private Set<Long> materialsIds;
    private Set<Long> colorsIds;
    private Boolean hasPrint;

    private Integer price;

    private List<CreateSizeQuantityRequestDto> sizesQuantities;
}
