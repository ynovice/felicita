package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "reserves")
@Getter
@Setter
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "reserve", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReserveEntry> entries;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private Integer totalItems;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private User user;

    public void updateTotalItems(int difference) {
        totalItems += difference;
    }

    public void updateTotalPrice(int difference) {
        totalPrice += difference;
    }
}
