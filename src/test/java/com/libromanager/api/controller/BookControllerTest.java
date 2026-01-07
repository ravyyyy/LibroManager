package com.libromanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libromanager.api.dto.BookRequestDTO;
import com.libromanager.api.entity.Book;
import com.libromanager.api.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateBook() throws Exception {
        BookRequestDTO request = new BookRequestDTO();
        request.setTitle("Baltagul");
        request.setIsbn("111-222");
        request.setStock(10);
        request.setPublishYear(1930);
        request.setPublisherId(1L);
        request.setAuthorIds(Set.of(1L));

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Baltagul");

        when(bookService.addBook(any(BookRequestDTO.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Baltagul"));
    }

    @Test
    void testGetAllBooks() throws Exception {
        Book b1 = new Book();
        b1.setTitle("Cartea 1");

        when(bookService.getAllBooks()).thenReturn(List.of(b1));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk()) // Asteptam 200 OK
                .andExpect(jsonPath("$[0].title").value("Cartea 1"));
    }
}