package com.example.book_catalog.controller;

import com.example.book_catalog.dto.*;
//import com.example.book_catalog.mapper.BookMapper;
import com.example.book_catalog.model.entity.Book;
import com.example.book_catalog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
//    private final BookMapper mapper;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAll().stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublishedDate(),
                        book.getPrice(),
                        book.getAuthors().stream()
                                .map(author -> new AuthorDto(author.getId(), author.getFirstName(), author.getLastName()))
                                .collect(Collectors.toSet()),
                        book.getCategories().stream()
                                .map(category -> new CategoryDto(category.getId(), category.getName()))
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookDto getOne(@PathVariable Long id) {
        Book book = bookService.getById(id);
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublishedDate(),
                book.getPrice(),
                book.getAuthors().stream()
                        .map(author -> new AuthorDto(author.getId(), author.getFirstName(), author.getLastName()))
                        .collect(Collectors.toSet()),
                book.getCategories().stream()
                        .map(category -> new CategoryDto(category.getId(), category.getName()))
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/search")
    public List<BookDto> search(@RequestParam String title) {
        return bookService.searchByTitle(title).stream()
                .map(book -> new BookDto(
                        book.getId(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPublishedDate(),
                        book.getPrice(),
                        book.getAuthors().stream()
                                .map(author -> new AuthorDto(author.getId(), author.getFirstName(), author.getLastName()))
                                .collect(Collectors.toSet()),
                        book.getCategories().stream()
                                .map(category -> new CategoryDto(category.getId(), category.getName()))
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Validated @RequestBody BookRequest req) {
        Book book = Book.builder()
                .title(req.getTitle())
                .isbn(req.getIsbn())
                .publishedDate(req.getPublishedDate())
                .price(req.getPrice())
                .authors(new HashSet<>(authorService.findAllByIds(req.getAuthorIds())))
                .categories(new HashSet<>(categoryService.findAllByIds(req.getCategoryIds())))
                .build();

        Book savedBook = bookService.create(book);
        return new BookDto(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getIsbn(),
                savedBook.getPublishedDate(),
                savedBook.getPrice(),
                savedBook.getAuthors().stream()
                        .map(author -> new AuthorDto(author.getId(), author.getFirstName(), author.getLastName()))
                        .collect(Collectors.toSet()),
                savedBook.getCategories().stream()
                        .map(category -> new CategoryDto(category.getId(), category.getName()))
                        .collect(Collectors.toSet())
        );
    }

    @PutMapping("/{id}")
    public BookDto update(
            @PathVariable Long id,
            @Validated @RequestBody BookRequest req
    ) {
        Book book = Book.builder()
                .title(req.getTitle())
                .isbn(req.getIsbn())
                .publishedDate(req.getPublishedDate())
                .price(req.getPrice())
                .authors(new HashSet<>(authorService.findAllByIds(req.getAuthorIds())))
                .categories(new HashSet<>(categoryService.findAllByIds(req.getCategoryIds())))
                .build();

        Book updatedBook = bookService.update(id, book);
        return new BookDto(
                updatedBook.getId(),
                updatedBook.getTitle(),
                updatedBook.getIsbn(),
                updatedBook.getPublishedDate(),
                updatedBook.getPrice(),
                updatedBook.getAuthors().stream()
                        .map(author -> new AuthorDto(author.getId(), author.getFirstName(), author.getLastName()))
                        .collect(Collectors.toSet()),
                updatedBook.getCategories().stream()
                        .map(category -> new CategoryDto(category.getId(), category.getName()))
                        .collect(Collectors.toSet())
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
