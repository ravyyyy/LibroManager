package com.libromanager.api.service;

import com.libromanager.api.dto.BookRequestDTO;
import com.libromanager.api.entity.Author;
import com.libromanager.api.entity.Book;
import com.libromanager.api.entity.Publisher;
import com.libromanager.api.repository.AuthorRepository;
import com.libromanager.api.repository.BookRepository;
import com.libromanager.api.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testAddBook_Success() {
        BookRequestDTO request = new BookRequestDTO();
        request.setTitle("Ion");
        request.setIsbn("978-123");
        request.setPublishYear(1920);
        request.setStock(5);
        request.setPublisherId(1L);
        request.setAuthorIds(Set.of(1L));

        Publisher mockPublisher = new Publisher();
        mockPublisher.setId(1L);
        mockPublisher.setName("Humanitas");

        Author mockAuthor = new Author();
        mockAuthor.setId(1L);
        mockAuthor.setFullName("Rebreanu");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Ion");
        savedBook.setPublisher(mockPublisher);

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(mockPublisher));
        when(authorRepository.findAllById(any())).thenReturn(List.of(mockAuthor));
        when(bookRepository.save(any())).thenReturn(savedBook);

        Book result = bookService.addBook(request);

        assertNotNull(result, "The result should not be null");
        assertEquals("Ion", result.getTitle());
        assertEquals(1L, result.getId());

        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void testAddBook_PublisherNotFound() {
        BookRequestDTO request = new BookRequestDTO();
        request.setPublisherId(99L);
        request.setAuthorIds(Set.of(1L));

        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.addBook(request);
        });

        assertEquals("Publisher does not exist!", exception.getMessage());

        verify(bookRepository, never()).save(any());
    }

    @Test
    void testAddBook_NoAuthorsFound() {
        BookRequestDTO request = new BookRequestDTO();
        request.setPublisherId(1L);
        request.setAuthorIds(Set.of(1L));

        Publisher mockPublisher = new Publisher();
        mockPublisher.setId(1L);

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(mockPublisher));
        when(authorRepository.findAllById(any())).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.addBook(request);
        });

        assertEquals("No valid author was found!", exception.getMessage());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testUpdateBook() {
        Long id = 1L;
        Book existing = new Book();
        existing.setId(id);
        existing.setPublisher(new Publisher(1L, "P", "A", null));

        BookRequestDTO request = new BookRequestDTO();
        request.setTitle("New Title");
        request.setPublisherId(1L);
        request.setAuthorIds(Set.of(1L));

        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.findAllById(any())).thenReturn(List.of(new Author()));
        when(bookRepository.save(any())).thenReturn(existing);

        Book result = bookService.updateBook(id, request);
        assertEquals("New Title", result.getTitle());
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        bookService.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }
}