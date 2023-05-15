package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDto {

    private Long id;
    private String name;

    private Long parentId;
    private List<CategoryDto> subCategories;

    public static CategoryDto fromEntity(Category category) {

        if(category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setParentId(category.getParentId());

        dto.setSubCategories(
                category.getSubCategories()
                        .stream()
                        .map(CategoryDto::fromEntity)
                        .toList()
        );

        return dto;
    }
}
