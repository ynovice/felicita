package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
