package com.example.book_catalog.service.impl;

import com.example.book_catalog.exception.NotFoundException;
import com.example.book_catalog.model.entity.Author;
import com.example.book_catalog.repository.AuthorRepository;
import com.example.book_catalog.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found, id=" + id));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author update(Long id, Author updated) {
        Author existing = getById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        return authorRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author a = getById(id);
        authorRepository.delete(a);
    }

    @Override
    public List<Author> searchByLastName(String lastNamePart) {
        return authorRepository.findByLastNameContainingIgnoreCase(lastNamePart);
    }

    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        List<Author> list = authorRepository.findAllById(ids);
        if (list.size() != ids.size()) {
            throw new NotFoundException("Some authors not found: " + ids);
        }
        return list;
    }
}
