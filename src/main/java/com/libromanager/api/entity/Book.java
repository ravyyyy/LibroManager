package com.libromanager.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Title is mandatory")
    @Size(min = 2, max = 100)
    private String title;

    @NotEmpty(message = "ISBN is mandatory")
    @Column(unique = true, nullable = false)
    private String isbn;

    @Min(1800)
    private Integer publishYear;

    @Min(0)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    @JsonIgnoreProperties("books")
    @ToString.Exclude
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnoreProperties("books")
    @ToString.Exclude
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("book")
    private List<Review> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("book")
    private List<Loan> loans;
}
