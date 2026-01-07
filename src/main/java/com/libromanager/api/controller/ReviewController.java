package com.libromanager.api.controller;

import com.libromanager.api.dto.ReviewRequestDTO;
import com.libromanager.api.entity.Review;
import com.libromanager.api.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Endpoints for managing book reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @Operation(summary = "Add a review", description = "Adds a rating (1-5) and comment for a book by a reader.")
    public ResponseEntity<Review> addReview(@Valid @RequestBody ReviewRequestDTO request) {
        return new ResponseEntity<>(reviewService.addReview(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all reviews", description = "Returns all reviews in the system.")
    public ResponseEntity<List<Review>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get reviews by book", description = "Returns all reviews associated with a specific book ID.")
    public ResponseEntity<List<Review>> getReviewsByBook(@PathVariable Long bookId) {
        return new ResponseEntity<>(reviewService.getReviewsForBook(bookId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit a review", description = "Updates the comment or rating of an existing review.")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequestDTO request) {
        return new ResponseEntity<>(reviewService.updateReview(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", description = "Removes a review permanently.")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
