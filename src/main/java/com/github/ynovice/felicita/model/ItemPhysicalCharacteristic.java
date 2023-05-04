package com.github.ynovice.felicita.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "items_physical_characteristics")
@Getter
@Setter
public class ItemPhysicalCharacteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "characteristic_materials",
            joinColumns = {@JoinColumn(name = "characteristic_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "material_id", referencedColumnName = "id")}
    )
    private Set<Material> materials;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "characteristic_colors",
            joinColumns = {@JoinColumn(name = "characteristic_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "color_id", referencedColumnName = "id")}
    )
    private Set<Color> colors;

    private boolean hasPrint;

    @OneToOne(mappedBy = "characteristic", optional = false, fetch = FetchType.LAZY)
    private Item item;
}
