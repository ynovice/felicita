package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Cart;
import com.github.ynovice.felicita.model.entity.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartEntryRepository extends JpaRepository<CartEntry, Long> {

    void deleteAllByCart(Cart cart);
}
