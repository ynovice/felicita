package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "cart_entries",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cart_id", "item_id"})
        }
)
@Getter
@Setter
public class CartEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Item item;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cartEntry", cascade = CascadeType.ALL)
    private List<SizeQuantity> sizesQuantities;

    @ElementCollection
    @CollectionTable(
            name = "cart_entries_sizes_quantities_prev_states",
            joinColumns = @JoinColumn(name = "cart_entry_id", referencedColumnName = "id")
    )
    private List<SizeQuantityPrevState> sizesQuantitiesPrevStates;

    @Deprecated
    public Long getUserId() {
        return cart != null ? cart.getUserId() : null;
    }

    public Long getItemId() {
        return item != null ? item.getId() : null;
    }

    public void addSizeQuantityPrevState(SizeQuantityPrevState sqps) {

        if(this.sizesQuantitiesPrevStates == null)
            sizesQuantitiesPrevStates = new ArrayList<>();

        sizesQuantitiesPrevStates.add(sqps);
    }

    public SizeQuantity getSizeQuantityBySize(@NonNull Size size) {
        return sizesQuantities
                .stream()
                .filter(sq -> sq.getSize().equals(size))
                .findFirst()
                .orElseGet(() -> createAndLinkSizeQuantity(size));
    }

    private SizeQuantity createAndLinkSizeQuantity(@NonNull Size size) {

        SizeQuantity sizeQuantity = new SizeQuantity();
        sizeQuantity.setSize(size);
        sizeQuantity.setQuantity(0);
        sizeQuantity.setCartEntry(this);

        sizesQuantities.add(sizeQuantity);
        return sizeQuantity;
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SizeQuantityPrevState {

        @ManyToOne
        @JoinColumn(referencedColumnName = "id", nullable = false)
        private Size size;

        @Column(nullable = false)
        private Integer quantity;

        public SizeQuantityPrevState(SizeQuantity source) {
            this(source.getSize(), source.getQuantity());
        }
    }
}
