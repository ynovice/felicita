package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "reserves")
@Getter
@Setter
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Item item;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reserve", cascade = CascadeType.ALL)
    private Set<SizeQuantity> sizesQuantities;

    @Column(nullable = false)
    private Integer pricePerItem;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private User user;
}
