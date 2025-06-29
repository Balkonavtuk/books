package com.example.book_catalog.service.impl;

import com.example.book_catalog.exception.NotFoundException;
import com.example.book_catalog.model.entity.Category;
import com.example.book_catalog.repository.CategoryRepository;
import com.example.book_catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found, id=" + id));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category update(Long id, Category updated) {
        Category existing = getById(id);
        existing.setName(updated.getName());
        return categoryRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category c = getById(id);
        categoryRepository.delete(c);
    }

    @Override
    public List<Category> findAllByIds(Set<Long> ids) {
        List<Category> list = categoryRepository.findAllById(ids);
        if (list.size() != ids.size()) {
            throw new NotFoundException("Some categories not found: " + ids);
        }
        return list;
    }

}
