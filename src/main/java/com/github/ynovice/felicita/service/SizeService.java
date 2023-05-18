package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Size;

import java.util.List;
import java.util.Optional;

public interface SizeService {

    List<Size> getAll();

    Optional<Size> getById(Long id);
}
