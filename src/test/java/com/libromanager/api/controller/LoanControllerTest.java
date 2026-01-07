package com.libromanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libromanager.api.dto.LoanRequestDTO;
import com.libromanager.api.entity.Loan;
import com.libromanager.api.service.LoanService;
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

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateLoan() throws Exception {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setReaderId(1L);
        request.setBookId(1L);

        Loan savedLoan = new Loan();
        savedLoan.setId(10L);
        savedLoan.setStatus(Loan.LoanStatus.ACTIVE);

        when(loanService.createLoan(any(LoanRequestDTO.class))).thenReturn(savedLoan);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testGetAllLoans() throws Exception {
        when(loanService.getAllLoans()).thenReturn(List.of(new Loan()));

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}