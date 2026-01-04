package com.libromanager.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
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
    @Column(unique = true)
    private String isbn;

    @Min(1800)
    private Integer publishYear;

    @Min(0)
    private Integer stock;
}
