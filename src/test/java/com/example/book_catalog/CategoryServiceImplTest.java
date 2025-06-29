package com.example.book_catalog;

import com.example.book_catalog.exception.NotFoundException;
import com.example.book_catalog.model.entity.Category;
import com.example.book_catalog.repository.CategoryRepository;
import com.example.book_catalog.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl service;

    private Category sample;

    @BeforeEach
    void setUp() {
        sample = new Category();
        sample.setId(1L);
        sample.setName("Fiction");
    }

    @Test
    void create_shouldSaveAndReturn() {
        when(categoryRepository.save(sample)).thenReturn(sample);

        Category result = service.create(sample);

        assertThat(result).isSameAs(sample);
        verify(categoryRepository).save(sample);
    }

    @Test
    void getById_whenFound_returnsCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sample));

        Category result = service.getById(1L);

        assertThat(result).isSameAs(sample);
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getById_whenNotFound_throwsNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category not found, id=99");
    }

    @Test
    void getAll_returnsAllCategories() {
        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("Science");
        List<Category> list = Arrays.asList(sample, c2);

        when(categoryRepository.findAll()).thenReturn(list);

        List<Category> result = service.getAll();

        assertThat(result).containsExactly(sample, c2);
        verify(categoryRepository).findAll();
    }

    @Test
    void update_whenFound_updatesNameAndSaves() {
        Category updated = new Category();
        updated.setName("Adventure");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(categoryRepository.save(sample)).thenReturn(sample);

        Category result = service.update(1L, updated);

        assertThat(result.getName()).isEqualTo("Adventure");
        verify(categoryRepository).save(sample);
    }

    @Test
    void update_whenNotFound_throwsNotFoundException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(2L, sample))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category not found, id=2");
    }

    @Test
    void delete_whenFound_deletesCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sample));

        service.delete(1L);

        verify(categoryRepository).delete(sample);
    }

    @Test
    void delete_whenNotFound_throwsNotFoundException() {
        when(categoryRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(5L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category not found, id=5");
    }

    @Test
    void findAllByIds_allFound_returnsList() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));
        Category c2 = new Category();
        c2.setId(2L);
        List<Category> found = Arrays.asList(sample, c2);

        when(categoryRepository.findAllById(ids)).thenReturn(found);

        List<Category> result = service.findAllByIds(ids);

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(sample, c2);
        verify(categoryRepository).findAllById(ids);
    }

    @Test
    void findAllByIds_someMissing_throwsNotFoundException() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));
        when(categoryRepository.findAllById(ids)).thenReturn(Collections.singletonList(sample));

        assertThatThrownBy(() -> service.findAllByIds(ids))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Some categories not found");
    }
}