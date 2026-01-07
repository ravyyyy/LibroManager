package com.libromanager.api.service;

import com.libromanager.api.dto.BookRequestDTO;
import com.libromanager.api.entity.Author;
import com.libromanager.api.entity.Book;
import com.libromanager.api.entity.Publisher;
import com.libromanager.api.repository.AuthorRepository;
import com.libromanager.api.repository.BookRepository;
import com.libromanager.api.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }

    public Book addBook(BookRequestDTO request) {
        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher does not exist!"));

        List<Author> foundAuthors = authorRepository.findAllById(request.getAuthorIds());
        if (foundAuthors.isEmpty()) {
            throw new RuntimeException("No valid author was found!");
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublishYear(request.getPublishYear());
        book.setStock(request.getStock());
        book.setPublisher(publisher);
        book.setAuthors(new HashSet<>(foundAuthors));

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBookByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}
