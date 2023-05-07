package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {}
