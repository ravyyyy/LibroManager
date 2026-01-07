package com.libromanager.api.service;

import com.libromanager.api.entity.Author;
import com.libromanager.api.entity.Book;
import com.libromanager.api.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository);
    }

    @Test
    void testGetAllAuthors() {
        when(authorRepository.findAll(any(Sort.class))).thenReturn(List.of(new Author()));

        List<Author> authors = authorService.getAllAuthors();

        assertEquals(1, authors.size());
        verify(authorRepository).findAll(any(Sort.class));
    }

    @Test
    void testUpdateAuthor() {
        Long id = 1L;
        Author existingAuthor = new Author(id, "Ion Creanga", "Roman", new HashSet<>());
        Author updateInfo = new Author();
        updateInfo.setFullName("Ion Creanga Updated");
        updateInfo.setNationality("Romanian");

        when(authorRepository.findById(id)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        Author result = authorService.updateAuthor(id, updateInfo);

        assertEquals("Ion Creanga Updated", result.getFullName());
        assertEquals("Romanian", result.getNationality());
    }

    @Test
    void testDeleteAuthor_WithUnlink() {
        Long id = 1L;
        Author author = new Author(id, "Test Author", "Test", new HashSet<>());

        Book book = new Book();
        book.setAuthors(new HashSet<>(Set.of(author)));
        author.setBooks(new HashSet<>(Set.of(book)));

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        authorService.deleteAuthor(id);

        assertTrue(book.getAuthors().isEmpty());
        verify(authorRepository).delete(author);
    }
}