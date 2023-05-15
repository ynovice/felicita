package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentIsNull();

    @Query("select c.parent.id from Category c where c.id = ?1")
    Optional<Long> findParentIdById(Long id);
}
