package com.libromanager.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequestDTO {
    @NotNull(message = "The ID of the reader is mandatory")
    private Long readerId;

    @NotNull(message = "The ID of the book is mandatory")
    private Long bookId;
}
