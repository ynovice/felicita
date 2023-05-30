package com.github.ynovice.felicita.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "preview_id", referencedColumnName = "id")
    private Image preview;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 50000)
    private String content;
}
