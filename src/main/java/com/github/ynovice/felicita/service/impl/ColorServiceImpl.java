package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.entity.Color;
import com.github.ynovice.felicita.repository.ColorRepository;
import com.github.ynovice.felicita.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public List<Color> getAll() {
        return colorRepository.findAll();
    }
}
