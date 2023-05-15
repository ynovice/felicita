package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.entity.Category;
import com.github.ynovice.felicita.repository.CategoryRepository;
import com.github.ynovice.felicita.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllRootcategories() {
        return categoryRepository.findAllByParentIsNull();
    }
}
