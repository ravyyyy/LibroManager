package com.libromanager.api.service;

import com.libromanager.api.dto.ReviewRequestDTO;
import com.libromanager.api.entity.Book;
import com.libromanager.api.entity.Reader;
import com.libromanager.api.entity.Review;
import com.libromanager.api.repository.BookRepository;
import com.libromanager.api.repository.ReaderRepository;
import com.libromanager.api.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, ReaderRepository readerRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    public Review addReview(ReviewRequestDTO request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("The book does not exist!"));

        Reader reader = readerRepository.findById(request.getReaderId())
                .orElseThrow(() -> new RuntimeException("The reader does not exist!"));

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setBook(book);
        review.setReader(reader);

        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsForBook(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }
}
