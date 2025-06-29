package com.example.book_catalog.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private Long id;
    private String title;
    private String isbn;
    private LocalDate publishedDate;
    private BigDecimal price;
    private Set<AuthorDto> authors;
    private Set<CategoryDto> categories;
}
