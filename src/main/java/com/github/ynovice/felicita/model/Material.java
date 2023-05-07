package com.github.ynovice.felicita.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "materials")
@Getter
@Setter
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @ManyToMany(mappedBy = "materials", fetch = FetchType.LAZY)
    private Set<Item> relatedItems;
}
