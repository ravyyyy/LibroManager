package com.libromanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libromanager.api.dto.ReviewRequestDTO;
import com.libromanager.api.entity.Review;
import com.libromanager.api.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddReview() throws Exception {
        ReviewRequestDTO request = new ReviewRequestDTO();
        request.setBookId(1L);
        request.setReaderId(1L);
        request.setRating(5);
        request.setComment("Super!");

        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setRating(5);

        when(reviewService.addReview(any(ReviewRequestDTO.class))).thenReturn(savedReview);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testGetReviewsByBook() throws Exception {
        Review r1 = new Review();
        r1.setComment("Test Review");

        when(reviewService.getReviewsForBook(eq(5L))).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/reviews/book/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value("Test Review"));
    }
}