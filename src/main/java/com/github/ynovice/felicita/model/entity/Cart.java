package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartEntry> entries;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private Integer totalItems;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", unique = true, nullable = false)
    private User user;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public CartEntry getCartEntryByItem(@NonNull Item item) {

        return entries
                .stream()
                .filter(ce -> ce.getItem().equals(item))
                .findFirst()
                .orElseGet(() -> createAndLinkCartEntry(item));
    }

    public void updateTotalItems(int difference) {
        totalItems += difference;
    }

    public void updateTotalPrice(int difference) {
        totalPrice += difference;
    }

    private CartEntry createAndLinkCartEntry(Item item) {

        CartEntry cartEntry = new CartEntry();
        cartEntry.setCart(this);
        cartEntry.setItem(item);
        cartEntry.setSizesQuantities(new ArrayList<>());
        cartEntry.setSizesQuantitiesPrevStates(new ArrayList<>());

        entries.add(cartEntry);
        return cartEntry;
    }
}
