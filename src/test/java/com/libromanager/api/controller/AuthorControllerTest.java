package com.libromanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libromanager.api.entity.Author;
import com.libromanager.api.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAuthor() throws Exception {
        Author author = new Author();
        author.setFullName("Mihai Eminescu");
        author.setNationality("Roman");

        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setFullName("Mihai Eminescu");

        when(authorService.addAuthor(any(Author.class))).thenReturn(savedAuthor);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Mihai Eminescu"));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        Author a1 = new Author();
        a1.setFullName("Creanga");

        when(authorService.getAllAuthors()).thenReturn(List.of(a1));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Creanga"));
    }
}