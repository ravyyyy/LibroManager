package com.libromanager.api.controller;

import com.libromanager.api.entity.Reader;
import com.libromanager.api.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping
    public ResponseEntity<Reader> registerReader(@Valid @RequestBody Reader reader) {
        return new ResponseEntity<>(readerService.registerReader(reader), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        return new ResponseEntity<>(readerService.getAllReaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @Valid @RequestBody Reader reader) {
        return new ResponseEntity<>(readerService.updateReader(id, reader), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
