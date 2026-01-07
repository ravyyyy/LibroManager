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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        ReviewRequestDTO req = new ReviewRequestDTO();
        req.setBookId(1L); req.setReaderId(1L); req.setComment("Ok"); req.setRating(5);

        Review saved = new Review(); saved.setRating(5);

        when(reviewService.addReview(any())).thenReturn(saved);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testGetReviewsByBook() throws Exception {
        when(reviewService.getReviewsForBook(1L)).thenReturn(List.of(new Review()));
        mockMvc.perform(get("/api/reviews/book/1")).andExpect(status().isOk());
    }

    @Test
    void testUpdateReview() throws Exception {
        ReviewRequestDTO updateReq = new ReviewRequestDTO();
        updateReq.setComment("Updated Comment");
        updateReq.setRating(4);
        updateReq.setBookId(1L);
        updateReq.setReaderId(1L);

        Review updated = new Review();
        updated.setComment("Updated Comment");

        when(reviewService.updateReview(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Updated Comment"));
    }

    @Test
    void testDeleteReview() throws Exception {
        doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }
}