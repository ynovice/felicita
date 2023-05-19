package com.github.ynovice.felicita.model.dto.response;

import com.github.ynovice.felicita.model.dto.entity.ItemDto;
import com.github.ynovice.felicita.model.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ItemsPageDto {

    private List<ItemDto> items;
    private PaginationMeta paginationMeta;

    public static ItemsPageDto fromEntity(Page<Item> itemsPage) {

        ItemsPageDto dto = new ItemsPageDto();
        dto.setItems(itemsPage.stream().map(ItemDto::fromEntity).toList());
        dto.setPaginationMeta(PaginationMeta.fromEntity(itemsPage));

        return dto;
    }

    @Getter
    @Setter
    public static class PaginationMeta {

        private int currentPage;
        private int totalPages;
        private int elementsPerPage;

        public static PaginationMeta fromEntity(Page<Item> itemsPage) {

            PaginationMeta dto = new PaginationMeta();
            dto.setCurrentPage(itemsPage.getNumber());
            dto.setTotalPages(itemsPage.getTotalPages());
            dto.setElementsPerPage(itemsPage.getSize());

            return dto;
        }
    }
}
