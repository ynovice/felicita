package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.CartEntry;
import com.github.ynovice.felicita.model.entity.SizeQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeQuantityRepository extends JpaRepository<SizeQuantity, Long> {

    void deleteAllByCartEntry(CartEntry cartEntry);
}
