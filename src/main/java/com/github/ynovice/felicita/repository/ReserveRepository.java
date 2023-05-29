package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Reserve;
import com.github.ynovice.felicita.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    List<Reserve> findAllByUser(User user);
}
