package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Item;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ItemDto {

    private Long id;
    private String name;
    private String description;

    private List<ImageDto> images;
    private Set<CategoryDto> categories;
    private Set<MaterialDto> materials;
    private Set<ColorDto> colors;

    private Boolean hasPrint;
    private ZonedDateTime createdAt;
    private Integer price;
    private Boolean active;
    private List<SizeQuantityDto> sizesQuantities;

    public static ItemDto fromEntity(Item item) {

        if(item == null) return null;

        ItemDto dto = new ItemDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());

        dto.setImages(
                item.getImages()
                        .stream()
                        .map(ImageDto::fromEntity)
                        .toList()
        );

        dto.setCategories(
                item.getCategories()
                        .stream()
                        .map(CategoryDto::fromEntity)
                        .collect(Collectors.toSet())
        );

        dto.setMaterials(
                item.getMaterials()
                        .stream()
                        .map(MaterialDto::fromEntity)
                        .collect(Collectors.toSet())
        );

        dto.setColors(
                item.getColors()
                        .stream()
                        .map(ColorDto::fromEntity)
                        .collect(Collectors.toSet())
        );

        dto.setHasPrint(item.getHasPrint());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setPrice(item.getPrice());
        dto.setActive(item.getActive());

        dto.setSizesQuantities(
                item.getSizesQuantities()
                        .stream()
                        .map(SizeQuantityDto::fromEntity)
                        .toList()
        );

        return dto;
    }
}
