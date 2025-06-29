package com.example.book_catalog;


import com.example.book_catalog.exception.NotFoundException;
import com.example.book_catalog.model.entity.Book;
import com.example.book_catalog.repository.BookRepository;
import com.example.book_catalog.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl service;

    private Book sample;

    @BeforeEach
    void setUp() {
        sample = new Book();
        sample.setId(1L);
        sample.setTitle("Effective Java");
        sample.setIsbn("978-0134685991");
        sample.setPublishedDate(LocalDate.of(2018, 1, 6));
        sample.setPrice(new BigDecimal("45.00"));
        sample.setAuthors(new HashSet<>());
        sample.setCategories(new HashSet<>());
    }

    @Test
    void create_savesAndReturns() {
        when(bookRepository.save(sample)).thenReturn(sample);

        Book result = service.create(sample);

        assertThat(result).isSameAs(sample);
        verify(bookRepository).save(sample);
    }

    @Test
    void getById_whenFound_returnsBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sample));

        Book result = service.getById(1L);

        assertThat(result).isSameAs(sample);
        verify(bookRepository).findById(1L);
    }

    @Test
    void getById_whenNotFound_throwsNotFound() {
        when(bookRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(42L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book not found, id=42");
    }

    @Test
    void getAll_returnsAllBooks() {
        Book b2 = new Book();
        b2.setId(2L);
        List<Book> list = Arrays.asList(sample, b2);
        when(bookRepository.findAll()).thenReturn(list);

        List<Book> result = service.getAll();

        assertThat(result).hasSize(2).containsExactly(sample, b2);
        verify(bookRepository).findAll();
    }

    @Test
    void update_whenFound_updatesAndSaves() {
        Book updated = new Book();
        updated.setTitle("Java Concurrency in Practice");
        updated.setIsbn("978-0321349606");
        updated.setPublishedDate(LocalDate.of(2006, 5, 9));
        updated.setPrice(new BigDecimal("40.00"));
        updated.setAuthors(Collections.emptySet());
        updated.setCategories(Collections.emptySet());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Book result = service.update(1L, updated);

        assertThat(result.getTitle()).isEqualTo("Java Concurrency in Practice");
        assertThat(result.getIsbn()).isEqualTo("978-0321349606");
        assertThat(result.getPublishedDate()).isEqualTo(LocalDate.of(2006, 5, 9));
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("40.00"));
        assertThat(result.getAuthors()).isEmpty();
        assertThat(result.getCategories()).isEmpty();
        verify(bookRepository).save(sample);
    }

    @Test
    void update_whenNotFound_throwsNotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(2L, sample))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book not found, id=2");
    }

    @Test
    void delete_whenFound_deletesBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sample));

        service.delete(1L);

        verify(bookRepository).delete(sample);
    }

    @Test
    void delete_whenNotFound_throwsNotFound() {
        when(bookRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(5L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Book not found, id=5");
    }

    @Test
    void searchByTitle_delegatesToRepository() {
        List<Book> matches = Collections.singletonList(sample);
        when(bookRepository.findByTitleContainingIgnoreCase("java"))
                .thenReturn(matches);

        List<Book> result = service.searchByTitle("java");

        assertThat(result).isSameAs(matches);
        verify(bookRepository).findByTitleContainingIgnoreCase("java");
    }
}