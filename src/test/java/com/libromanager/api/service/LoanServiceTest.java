package com.libromanager.api.service;

import com.libromanager.api.dto.LoanRequestDTO;
import com.libromanager.api.entity.Book;
import com.libromanager.api.entity.Loan;
import com.libromanager.api.entity.Reader;
import com.libromanager.api.repository.BookRepository;
import com.libromanager.api.repository.LoanRepository;
import com.libromanager.api.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReaderRepository readerRepository;

    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanService = new LoanService(loanRepository, bookRepository, readerRepository);
    }

    @Test
    void testCreateLoan_Success() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setBookId(1L);
        request.setReaderId(1L);

        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setStock(5);

        Reader mockReader = new Reader();
        mockReader.setId(1L);

        Loan savedLoan = new Loan();
        savedLoan.setId(1L);
        savedLoan.setStatus(Loan.LoanStatus.ACTIVE);
        savedLoan.setLoanDate(LocalDate.now());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(mockReader));
        when(loanRepository.countByReaderIdAndStatus(1L, Loan.LoanStatus.ACTIVE)).thenReturn(0L);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan result = loanService.createLoan(request);

        assertNotNull(result);
        assertEquals(Loan.LoanStatus.ACTIVE, result.getStatus());

        assertEquals(4, mockBook.getStock());
        verify(bookRepository, times(1)).save(mockBook);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testCreateLoan_BookOutOfStock() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setBookId(1L);
        request.setReaderId(1L);

        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setStock(0);

        Reader mockReader = new Reader();
        mockReader.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(mockReader));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(request);
        });

        assertEquals("This book is out of stock!", exception.getMessage());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void testCreateLoan_LimitReached() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setBookId(1L);
        request.setReaderId(1L);

        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setStock(5);

        Reader mockReader = new Reader();
        mockReader.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(mockReader));

        when(loanRepository.countByReaderIdAndStatus(1L, Loan.LoanStatus.ACTIVE)).thenReturn(3L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(request);
        });

        assertEquals("The reader already has 3 loans!", exception.getMessage());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void testCreateLoan_BookNotFound() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setBookId(99L);
        request.setReaderId(1L);

        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(request);
        });

        assertEquals("The book hasn't been found!", exception.getMessage());
    }

    @Test
    void testCreateLoan_ReaderNotFound() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setBookId(1L);
        request.setReaderId(99L);

        Book mockBook = new Book();
        mockBook.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(readerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.createLoan(request);
        });

        assertEquals("The reader hasn't been found!", exception.getMessage());
    }

    @Test
    void testUpdateLoan_ReturnBook() {
        Long id = 1L;
        Book book = new Book(); book.setStock(0);
        Loan loan = new Loan();
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setBook(book);

        Loan updateInfo = new Loan();
        updateInfo.setStatus(Loan.LoanStatus.COMPLETED);

        when(loanRepository.findById(id)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenReturn(loan);

        loanService.updateLoan(id, updateInfo);

        assertEquals(1, book.getStock());
        verify(bookRepository).save(book);
    }
}