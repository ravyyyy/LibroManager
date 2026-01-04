package com.libromanager.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is mandatory")
    @Column(nullable = false)
    private String fullName;

    private String nationality;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();
}
