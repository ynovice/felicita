package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.Cart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {

    private Long id;
    private Integer totalPrice;
    private Integer totalItems;
    private List<CartEntryDto> entries;

    public static CartDto fromEntity(Cart cart) {

        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setTotalItems(cart.getTotalItems());

        dto.setEntries(
                cart.getEntries()
                        .stream()
                        .map(CartEntryDto::fromEntity)
                        .toList()
        );

        return dto;
    }
}
