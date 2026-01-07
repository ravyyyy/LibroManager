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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        readerRequest.setEmail("ion@test.com");
        readerRequest.setLastName("Popescu");

        Reader savedReader = new Reader();
        savedReader.setId(1L);
        savedReader.setEmail("ion@test.com");

        when(readerService.registerReader(any(Reader.class))).thenReturn(savedReader);

        mockMvc.perform(post("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetAllReaders() throws Exception {
        when(readerService.getAllReaders()).thenReturn(List.of(new Reader()));

        mockMvc.perform(get("/api/readers"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateReader() throws Exception {
        Reader updateRequest = new Reader();
        updateRequest.setFirstName("Ion Updated");
        updateRequest.setLastName("Popescu");
        updateRequest.setEmail("ion@test.com");

        Reader updatedReader = new Reader();
        updatedReader.setId(1L);
        updatedReader.setFirstName("Ion Updated");

        when(readerService.updateReader(eq(1L), any(Reader.class))).thenReturn(updatedReader);

        mockMvc.perform(put("/api/readers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ion Updated"));
    }

    @Test
    void testDeleteReader() throws Exception {
        doNothing().when(readerService).deleteReader(1L);

        mockMvc.perform(delete("/api/readers/1"))
                .andExpect(status().isNoContent());
    }
}