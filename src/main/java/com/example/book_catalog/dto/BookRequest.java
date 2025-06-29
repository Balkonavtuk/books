package com.example.book_catalog.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 20)
    private String isbn;

    private LocalDate publishedDate;
    private BigDecimal price;

    @NotNull
    private Set<Long> authorIds;

    @NotNull
    private Set<Long> categoryIds;
}
