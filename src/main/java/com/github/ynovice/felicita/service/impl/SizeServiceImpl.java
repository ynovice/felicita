package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.entity.Size;
import com.github.ynovice.felicita.repository.SizeRepository;
import com.github.ynovice.felicita.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;

    @Override
    public List<Size> getAll() {
        return sizeRepository.findAll();
    }
}
