package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Reserve;
import com.github.ynovice.felicita.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    Page<Reserve> findAllByUser(User user, Pageable pageable);
}
