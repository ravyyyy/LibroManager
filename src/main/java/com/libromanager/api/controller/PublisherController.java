package com.libromanager.api.controller;

import com.libromanager.api.entity.Publisher;
import com.libromanager.api.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@Tag(name = "Publishers", description = "Endpoints for managing publishing houses")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    @Operation(summary = "Create a publisher", description = "Adds a new publisher.")
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) {
        return new ResponseEntity<>(publisherService.addPublisher(publisher), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all publishers", description = "Returns a list of all publishers.")
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return new ResponseEntity<>(publisherService.getAllPublishers(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a publisher", description = "Updates publisher details (Name, Address).")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @Valid @RequestBody Publisher publisher) {
        return new ResponseEntity<>(publisherService.updatePublisher(id, publisher), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a publisher", description = "Removes a publisher. This will cascade and delete all associated books.")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
