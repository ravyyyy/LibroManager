package com.libromanager.api.controller;

import com.libromanager.api.entity.Reader;
import com.libromanager.api.service.ReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
@Tag(name = "Readers", description = "Endpoints for registering and managing library members")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping
    @Operation(summary = "Register a reader", description = "Adds a new reader. Email must be unique.")
    public ResponseEntity<Reader> registerReader(@Valid @RequestBody Reader reader) {
        return new ResponseEntity<>(readerService.registerReader(reader), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all readers", description = "Returns a list of all registered readers.")
    public ResponseEntity<List<Reader>> getAllReaders() {
        return new ResponseEntity<>(readerService.getAllReaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update reader profile", description = "Updates reader details (Name, Email, Phone). Checks email uniqueness.")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @Valid @RequestBody Reader reader) {
        return new ResponseEntity<>(readerService.updateReader(id, reader), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reader", description = "Removes a reader and all their history (Loans and Reviews).")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
