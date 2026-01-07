package com.libromanager.api.service;

import com.libromanager.api.entity.Author;
import com.libromanager.api.entity.Book;
import com.libromanager.api.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author with id " + id + " was not found"));

        author.setFullName(authorDetails.getFullName());
        author.setNationality(authorDetails.getNationality());

        return authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author with id " + id + " was not found"));

        for (Book book : author.getBooks()) {
            book.getAuthors().remove(author);
        }

        authorRepository.delete(author);
    }
}
