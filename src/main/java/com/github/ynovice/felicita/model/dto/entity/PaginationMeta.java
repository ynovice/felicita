package com.github.ynovice.felicita.model.dto.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PaginationMeta {

    private int currentPage;
    private int totalPages;
    private int elementsPerPage;

    public static PaginationMeta fromEntity(Page<?> page) {

        PaginationMeta dto = new PaginationMeta();
        dto.setCurrentPage(page.getNumber());
        dto.setTotalPages(page.getTotalPages());
        dto.setElementsPerPage(page.getSize());

        return dto;
    }
}
