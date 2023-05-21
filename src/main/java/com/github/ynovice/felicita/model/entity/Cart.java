package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
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

    public Integer getQuantityByItemAndSize(Item item, Size size) {

        return entries
                .stream()
                .filter(ce -> ce.getItem().equals(item))
                .findFirst()
                .map(ce -> ce.getQuantityBySize(size))
                .orElse(0);
    }

    public CartEntry incrementItemQuantityBySize(Item item, Size size) {

        CartEntry cartEntry = entries
                .stream()
                .filter(ce -> ce.getItem().equals(item))
                .findFirst()
                .orElseGet(() -> {
                    CartEntry ce = buildCartEntry(item, this);
                    entries.add(ce);
                    return ce;
                });

        cartEntry.incrementQuantityBySize(size);

        totalItems++;
        totalPrice += item.getPrice();

        return cartEntry;
    }

    public CartEntry decrementItemQuantityBySize(Item item, Size size) {

        CartEntry cartEntry = entries
                .stream()
                .filter(ce -> ce.getItem().equals(item))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        cartEntry.decrementQuantityBySize(size);

        totalItems--;
        totalPrice -= item.getPrice();

        return cartEntry;
    }

    private CartEntry buildCartEntry(Item item, Cart cart) {
        CartEntry ce = new CartEntry();
        ce.setItem(item);
        ce.setCart(cart);
        ce.setSizesQuantities(new ArrayList<>());
        return ce;
    }
}
