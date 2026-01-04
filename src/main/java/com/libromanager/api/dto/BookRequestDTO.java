package com.libromanager.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class BookRequestDTO {
    @NotEmpty
    private String title;

    @NotEmpty
    private String isbn;

    @Min(1800)
    private Integer publishYear;

    @Min(0)
    private Integer stock;

    @NotNull(message = "Publisher is mandatory")
    private Long publisherId;

    @NotNull(message = "At least an author is necessary")
    private Set<Long> authorIds;
}
