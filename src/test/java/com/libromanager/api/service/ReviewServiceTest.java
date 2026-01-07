package com.libromanager.api.service;

import com.libromanager.api.dto.ReviewRequestDTO;
import com.libromanager.api.entity.Book;
import com.libromanager.api.entity.Reader;
import com.libromanager.api.entity.Review;
import com.libromanager.api.repository.BookRepository;
import com.libromanager.api.repository.ReaderRepository;
import com.libromanager.api.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReaderRepository readerRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(reviewRepository, bookRepository, readerRepository);
    }

    @Test
    void testAddReview_Success() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setBookId(1L);
        request.setReaderId(1L);
        request.setRating(5);
        request.setComment("Excellent!");

        Book mockBook = new Book();
        mockBook.setId(1L);

        Reader mockReader = new Reader();
        mockReader.setId(1L);

        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setComment("Excellent!");
        savedReview.setRating(5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(mockReader));
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        Review result = reviewService.addReview(request);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Excellent!", result.getComment());

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testAddReview_BookNotFound() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setBookId(99L);

        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.addReview(request);
        });

        assertEquals("The book does not exist!", exception.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testAddReview_ReaderNotFound() {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setBookId(1L);
        request.setReaderId(99L);

        Book mockBook = new Book();
        mockBook.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(readerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.addReview(request);
        });

        assertEquals("The reader does not exist!", exception.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testGetReviewsForBook() {
        Long bookId = 1L;
        Review review1 = new Review();
        review1.setComment("Cool");

        when(reviewRepository.findByBookId(bookId)).thenReturn(List.of(review1));

        List<Review> results = reviewService.getReviewsForBook(bookId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Cool", results.get(0).getComment());
    }

    @Test
    void testUpdateReview() {
        Long id = 1L;
        Review existing = new Review(); existing.setComment("Old");
        ReviewRequestDTO update = new ReviewRequestDTO(); update.setComment("New");

        when(reviewRepository.findById(id)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(any())).thenReturn(existing);

        Review res = reviewService.updateReview(id, update);
        assertEquals("New", res.getComment());
    }

    @Test
    void testDeleteReview() {
        when(reviewRepository.existsById(1L)).thenReturn(true);
        reviewService.deleteReview(1L);
        verify(reviewRepository).deleteById(1L);
    }
}