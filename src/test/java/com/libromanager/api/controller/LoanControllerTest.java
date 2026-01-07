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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        LoanRequestDTO req = new LoanRequestDTO();
        req.setBookId(1L); req.setReaderId(1L);

        Loan saved = new Loan(); saved.setId(1L); saved.setStatus(Loan.LoanStatus.ACTIVE);

        when(loanService.createLoan(any())).thenReturn(saved);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testGetAllLoans() throws Exception {
        when(loanService.getAllLoans()).thenReturn(List.of(new Loan()));
        mockMvc.perform(get("/api/loans")).andExpect(status().isOk());
    }

    @Test
    void testUpdateLoan() throws Exception {
        Loan updateReq = new Loan();
        updateReq.setStatus(Loan.LoanStatus.COMPLETED);

        Loan updated = new Loan();
        updated.setId(1L);
        updated.setStatus(Loan.LoanStatus.COMPLETED);

        when(loanService.updateLoan(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/loans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testDeleteLoan() throws Exception {
        doNothing().when(loanService).deleteLoan(1L);

        mockMvc.perform(delete("/api/loans/1"))
                .andExpect(status().isNoContent());
    }
}