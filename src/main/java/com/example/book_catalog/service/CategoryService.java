package com.example.book_catalog.service;

import com.example.book_catalog.model.entity.Category;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    Category create(Category category);
    Category getById(Long id);
    List<Category> getAll();
    Category update(Long id, Category category);
    void delete(Long id);
    List<Category> findAllByIds(Set<Long> ids);
}
