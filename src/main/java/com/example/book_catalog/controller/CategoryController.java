package com.example.book_catalog.controller;

import com.example.book_catalog.dto.CategoryDto;
import com.example.book_catalog.dto.CategoryRequest;
//import com.example.book_catalog.mapper.CategoryMapper;
import com.example.book_catalog.model.entity.Category;
import com.example.book_catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;
//    private final CategoryMapper mapper;

    @GetMapping
    public List<CategoryDto> getAll() {
        return service.getAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryDto getOne(@PathVariable Long id) {
        Category category = service.getById(id);
        return new CategoryDto(category.getId(), category.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Validated @RequestBody CategoryRequest req) {
        Category category = Category.builder().name(req.getName()).build();
        Category savedCategory = service.create(category);
        return new CategoryDto(savedCategory.getId(), savedCategory.getName());
    }

    @PutMapping("/{id}")
    public CategoryDto update(
            @PathVariable Long id,
            @Validated @RequestBody CategoryRequest req
    ) {
        Category category = Category.builder().name(req.getName()).build();
        Category updatedCategory = service.update(id, category);
        return new CategoryDto(updatedCategory.getId(), updatedCategory.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}