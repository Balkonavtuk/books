package com.example.book_catalog;

import com.example.book_catalog.exception.NotFoundException;
import com.example.book_catalog.model.entity.Author;
import com.example.book_catalog.repository.AuthorRepository;
import com.example.book_catalog.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl service;

    private Author sample;

    @BeforeEach
    void init() {
        sample = new Author();
        sample.setId(1L);
        sample.setFirstName("John");
        sample.setLastName("Doe");
    }

    @Test
    void create_shouldSaveAndReturn() {
        when(authorRepository.save(sample)).thenReturn(sample);

        Author result = service.create(sample);

        assertThat(result).isSameAs(sample);
        verify(authorRepository).save(sample);
    }

    @Test
    void getById_found() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sample));

        Author result = service.getById(1L);

        assertThat(result).isSameAs(sample);
        verify(authorRepository).findById(1L);
    }

    @Test
    void getById_notFound_throws() {
        when(authorRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(42L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author not found, id=42");
    }

    @Test
    void getAll_returnsList() {
        List<Author> authors = Arrays.asList(sample, new Author());
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = service.getAll();

        assertThat(result).hasSize(2).contains(sample);
        verify(authorRepository).findAll();
    }

    @Test
    void update_found_updatesFields() {
        Author updated = new Author();
        updated.setFirstName("Jane");
        updated.setLastName("Smith");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(authorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Author result = service.update(1L, updated);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        verify(authorRepository).save(sample);
    }

    @Test
    void update_notFound_throws() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(2L, sample))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author not found, id=2");
    }

    @Test
    void delete_found_deletes() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sample));

        service.delete(1L);

        verify(authorRepository).delete(sample);
    }

    @Test
    void delete_notFound_throws() {
        when(authorRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(5L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author not found, id=5");
    }

    @Test
    void searchByLastName_delegatesToRepo() {
        List<Author> matches = Collections.singletonList(sample);
        when(authorRepository.findByLastNameContainingIgnoreCase("doe"))
                .thenReturn(matches);

        List<Author> result = service.searchByLastName("doe");

        assertThat(result).isEqualTo(matches);
        verify(authorRepository).findByLastNameContainingIgnoreCase("doe");
    }

    @Test
    void findAllByIds_allFound_returnsList() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));
        Author a2 = new Author(); a2.setId(2L);
        List<Author> found = Arrays.asList(sample, a2);

        when(authorRepository.findAllById(ids)).thenReturn(found);

        List<Author> result = service.findAllByIds(ids);

        assertThat(result).hasSize(2).containsExactlyInAnyOrder(sample, a2);
        verify(authorRepository).findAllById(ids);
    }

    @Test
    void findAllByIds_someMissing_throws() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));
        // only one found
        when(authorRepository.findAllById(ids)).thenReturn(Collections.singletonList(sample));

        assertThatThrownBy(() -> service.findAllByIds(ids))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Some authors not found");
    }
}

