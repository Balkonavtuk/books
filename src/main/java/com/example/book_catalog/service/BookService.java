package com.example.book_catalog.service;

import com.example.book_catalog.model.entity.Book;
import java.util.List;

public interface BookService {
    Book create(Book book);
    Book getById(Long id);
    List<Book> getAll();
    Book update(Long id, Book book);
    void delete(Long id);
    List<Book> searchByTitle(String titlePart);
}
