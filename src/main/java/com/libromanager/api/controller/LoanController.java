package com.libromanager.api.controller;

import com.libromanager.api.dto.LoanRequestDTO;
import com.libromanager.api.entity.Loan;
import com.libromanager.api.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "Endpoints for managing book borrowing")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @Operation(summary = "Create a loan", description = "Borrows a book for a reader. Checks stock and loan limit (max 3). Decreases stock.")
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequestDTO loanRequest) {
        return new ResponseEntity<>(loanService.createLoan(loanRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all loans", description = "Returns the history of all loans (Active and Completed).")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return new ResponseEntity<>(loanService.getAllLoans(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update loan status", description = "Used primarily to mark a loan as COMPLETED (Return book), which restores stocks.")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        return new ResponseEntity<>(loanService.updateLoan(id, loan), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a loan", description = "Removes a loan record. If active, it restores the book stock.")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
