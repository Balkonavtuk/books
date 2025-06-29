package com.example.book_catalog.controller;

import com.example.book_catalog.dto.AuthorDto;
import com.example.book_catalog.dto.AuthorRequest;
//import com.example.book_catalog.mapper.AuthorMapper;
import com.example.book_catalog.model.entity.Author;
import com.example.book_catalog.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;
//    private final AuthorMapper mapper;

    @GetMapping
    public List<AuthorDto> getAll() {
        return service.getAll().stream()
                .map(author -> new AuthorDto(
                        author.getId(),
                        author.getFirstName(),
                        author.getLastName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AuthorDto getOne(@PathVariable Long id) {
        Author author = service.getById(id);
        return new AuthorDto(
                author.getId(),
                author.getFirstName(),
                author.getLastName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@Validated @RequestBody AuthorRequest req) {
        Author author = Author.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();

        Author savedAuthor = service.create(author);
        return new AuthorDto(
                savedAuthor.getId(),
                savedAuthor.getFirstName(),
                savedAuthor.getLastName());
    }

    @PutMapping("/{id}")
    public AuthorDto update(
            @PathVariable Long id,
            @Validated @RequestBody AuthorRequest req
    ) {
        Author author = Author.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();

        Author updatedAuthor = service.update(id, author);
        return new AuthorDto(
                updatedAuthor.getId(),
                updatedAuthor.getFirstName(),
                updatedAuthor.getLastName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

