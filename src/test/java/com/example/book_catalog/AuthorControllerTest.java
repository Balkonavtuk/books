package com.example.book_catalog;

import com.example.book_catalog.controller.AuthorController;
import com.example.book_catalog.dto.AuthorRequest;
import com.example.book_catalog.model.entity.Author;
import com.example.book_catalog.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorService service;

    @InjectMocks
    private AuthorController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAll_ShouldReturnListOfAuthorDto() throws Exception {
        Author author1 = Author.builder().id(1L).firstName("John").lastName("Doe").build();
        Author author2 = Author.builder().id(2L).firstName("Jane").lastName("Smith").build();
        when(service.getAll()).thenReturn(Arrays.asList(author1, author2));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getOne_ShouldReturnAuthorDto() throws Exception {
        Author author = Author.builder().id(1L).firstName("John").lastName("Doe").build();
        when(service.getById(1L)).thenReturn(author);

        mockMvc.perform(get("/api/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void create_ShouldReturnCreatedDto() throws Exception {
        AuthorRequest req = new AuthorRequest();
        req.setFirstName("John");
        req.setLastName("Doe");

        Author saved = Author.builder().id(1L).firstName("John").lastName("Doe").build();
        when(service.create(any(Author.class))).thenReturn(saved);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void update_ShouldReturnUpdatedDto() throws Exception {
        AuthorRequest req = new AuthorRequest();
        req.setFirstName("Jane");
        req.setLastName("Smith");

        Author updated = Author.builder().id(1L).firstName("Jane").lastName("Smith").build();
        when(service.update(eq(1L), any(Author.class))).thenReturn(updated);

        mockMvc.perform(put("/api/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
