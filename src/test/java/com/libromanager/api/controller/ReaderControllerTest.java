package com.libromanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libromanager.api.entity.Reader;
import com.libromanager.api.service.ReaderService;
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

@WebMvcTest(ReaderController.class)
class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderService readerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterReader() throws Exception {
        Reader readerRequest = new Reader();
        readerRequest.setFirstName("Ion");
        readerRequest.setLastName("Popescu");
        readerRequest.setEmail("ion@test.com");
        readerRequest.setPhoneNumber("0700000000");

        Reader savedReader = new Reader();
        savedReader.setId(1L);
        savedReader.setFirstName("Ion");
        savedReader.setEmail("ion@test.com");

        when(readerService.registerReader(any(Reader.class))).thenReturn(savedReader);

        mockMvc.perform(post("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("ion@test.com"));
    }

    @Test
    void testGetAllReaders() throws Exception {
        Reader r1 = new Reader();
        r1.setFirstName("Maria");

        when(readerService.getAllReaders()).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/readers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Maria"));
    }
}