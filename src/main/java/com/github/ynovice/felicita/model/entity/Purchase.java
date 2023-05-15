package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "purchases")
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Item item;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "purchases_sizes_quantities",
            joinColumns = {@JoinColumn(name = "purchase_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "size_quantity_id", referencedColumnName = "id")}
    )
    private Set<SizeQuantity> sizesQuantities;

    @Column(nullable = false)
    private Integer pricePerItem;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private User user;
}
