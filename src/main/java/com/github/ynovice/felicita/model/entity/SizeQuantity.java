package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sizes_quantities")
@NoArgsConstructor
@Getter
@Setter
public class SizeQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Size size;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private CartEntry cartEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Reserve reserve;

    public SizeQuantity(Size size, Integer quantity, Item item) {
        this.size = size;
        this.quantity = quantity;
        this.item = item;
    }

    public SizeQuantity(Size size, Integer quantity, CartEntry cartEntry) {
        this.size = size;
        this.quantity = quantity;
        this.cartEntry = cartEntry;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        quantity--;
    }

    public Long getSizeId() {
        return size == null ? null : size.getId();
    }
}
