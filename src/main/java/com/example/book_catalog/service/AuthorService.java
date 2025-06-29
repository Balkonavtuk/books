package com.example.book_catalog.service;

import com.example.book_catalog.model.entity.Author;
import java.util.List;
import java.util.Set;

public interface AuthorService {
    Author create(Author author);
    Author getById(Long id);
    List<Author> getAll();
    Author update(Long id, Author author);
    void delete(Long id);
    List<Author> searchByLastName(String lastNamePart);
    List<Author> findAllByIds(Set<Long> ids);
}
