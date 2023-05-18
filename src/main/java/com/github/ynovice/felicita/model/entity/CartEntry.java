package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Entity
@Table(
        name = "cart_entries",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "item_id"})
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
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Item item;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "cart_entries_sizes_quantities",
            joinColumns = {@JoinColumn(name = "cart_entry_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "size_quantity_id", referencedColumnName = "id")}
    )
    private List<SizeQuantity> sizesQuantities;

    @ElementCollection
    @CollectionTable(
            name = "cart_entries_sizes_quantities_prev_states",
            joinColumns = @JoinColumn(name = "cart_entry_id", referencedColumnName = "id")
    )
    private List<SizeQuantityPrevState> sizesQuantitiesPrevStates;

    public Integer getQuantityBySizeId(Long sizeId) {
        return getSizeQuantityBySizeId(sizeId)
                .map(SizeQuantity::getQuantity)
                .orElse(0);
    }

    public void incrementQuantityBySize(@NonNull Size size) {
        getSizeQuantityBySizeId(size.getId())
                .ifPresentOrElse(
                        SizeQuantity::incrementQuantity,
                        () -> sizesQuantities.add(new SizeQuantity(size, 1, this))
                );
    }

    public void removeSizeQuantityBySizeId(Long sizeId) {

        SizeQuantity sizeQuantity = getSizeQuantityBySizeId(sizeId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("SizeQuantity с cartEntryId=%d sizeId=%d не найден", getId(), sizeId)
                ));

        this.sizesQuantities.remove(sizeQuantity);
    }

    public Optional<SizeQuantity> getSizeQuantityBySizeId(Long sizeId) {
        return sizesQuantities
                .stream()
                .filter(sq -> sq.getSizeId().equals(sizeId))
                .findFirst();
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getItemId() {
        return item != null ? item.getId() : null;
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
    }
}
