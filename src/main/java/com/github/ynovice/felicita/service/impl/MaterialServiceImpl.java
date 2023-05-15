package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.entity.Material;
import com.github.ynovice.felicita.repository.MaterialRepository;
import com.github.ynovice.felicita.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public List<Material> getAll() {
        return materialRepository.findAll();
    }
}
