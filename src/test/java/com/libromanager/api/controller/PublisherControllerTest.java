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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        Publisher savedPublisher = new Publisher();
        savedPublisher.setId(1L);
        savedPublisher.setName("Humanitas");

        when(publisherService.addPublisher(any(Publisher.class))).thenReturn(savedPublisher);

        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publisher)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Humanitas"));
    }

    @Test
    void testGetAllPublishers() throws Exception {
        Publisher p1 = new Publisher();
        p1.setName("Nemira");

        when(publisherService.getAllPublishers()).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Nemira"));
    }
}