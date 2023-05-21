package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.CartEntry;
import com.github.ynovice.felicita.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartEntryRepository extends JpaRepository<CartEntry, Long> {

    @Query("select ce from CartEntry ce where ce.item.id = ?1 and ce.cart.user = ?2")
    Optional<CartEntry> findByItemIdAndUser(Long itemId, User user);
}
