package com.libromanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libromanager.api.entity.Publisher;
import com.libromanager.api.service.PublisherService;
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

@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePublisher() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setName("Humanitas");
        publisher.setAddress("Bucuresti");

        Publisher saved = new Publisher();
        saved.setId(1L);
        saved.setName("Humanitas");

        when(publisherService.addPublisher(any())).thenReturn(saved);

        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisher)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Humanitas"));
    }

    @Test
    void testGetAllPublishers() throws Exception {
        when(publisherService.getAllPublishers()).thenReturn(List.of(new Publisher()));
        mockMvc.perform(get("/api/publishers")).andExpect(status().isOk());
    }

    @Test
    void testUpdatePublisher() throws Exception {
        Publisher updateReq = new Publisher();
        updateReq.setName("Humanitas Updated");
        updateReq.setAddress("Cluj");

        Publisher updated = new Publisher();
        updated.setId(1L);
        updated.setName("Humanitas Updated");

        when(publisherService.updatePublisher(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/publishers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Humanitas Updated"));
    }

    @Test
    void testDeletePublisher() throws Exception {
        doNothing().when(publisherService).deletePublisher(1L);

        mockMvc.perform(delete("/api/publishers/1"))
                .andExpect(status().isNoContent());
    }
}