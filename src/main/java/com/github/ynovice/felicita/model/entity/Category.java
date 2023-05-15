package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id")
    private Category parent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private List<Category> subCategories;

    @ManyToMany(mappedBy = "categories")
    private List<Item> items;

    public Long getParentId() {
        return parent != null ? parent.getId() : null;
    }
}
