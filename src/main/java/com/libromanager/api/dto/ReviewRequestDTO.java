package com.libromanager.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDTO {

    @NotNull(message = "The ID of the book is mandatory")
    private Long bookId;

    @NotNull(message = "The ID of the reader is mandatory")
    private Long readerId;

    @Min(value = 1, message = "Min rating is 1")
    @Max(value = 5, message = "Max rating is 5")
    private Integer rating;

    @NotEmpty(message = "The comment can't be empty")
    private String comment;
}
