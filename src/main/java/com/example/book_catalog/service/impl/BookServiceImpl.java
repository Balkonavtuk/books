package com.example.book_catalog.service.impl;

import com.example.book_catalog.exception.NotFoundException;
import com.example.book_catalog.model.entity.Book;
import com.example.book_catalog.repository.BookRepository;
import com.example.book_catalog.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found, id=" + id));
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book update(Long id, Book updated) {
        Book existing = getById(id);
        existing.setTitle(updated.getTitle());
        existing.setIsbn(updated.getIsbn());
        existing.setPublishedDate(updated.getPublishedDate());
        existing.setPrice(updated.getPrice());
        existing.setAuthors(updated.getAuthors());
        existing.setCategories(updated.getCategories());
        return bookRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book b = getById(id);
        bookRepository.delete(b);
    }

    @Override
    public List<Book> searchByTitle(String titlePart) {
        return bookRepository.findByTitleContainingIgnoreCase(titlePart);
    }
}
