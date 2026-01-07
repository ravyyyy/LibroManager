package com.libromanager.api.service;

import com.libromanager.api.dto.LoanRequestDTO;
import com.libromanager.api.entity.Book;
import com.libromanager.api.entity.Loan;
import com.libromanager.api.entity.Reader;
import com.libromanager.api.repository.BookRepository;
import com.libromanager.api.repository.LoanRepository;
import com.libromanager.api.repository.ReaderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, ReaderRepository readerRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    @Transactional
    public Loan createLoan(LoanRequestDTO request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("The book hasn't been found!"));

        Reader reader = readerRepository.findById(request.getReaderId())
                .orElseThrow(() -> new RuntimeException("The reader hasn't been found!"));

        if (book.getStock() <= 0) {
            throw new RuntimeException("This book is out of stock!");
        }

        long activeLoans = loanRepository.countByReaderIdAndStatus(reader.getId(), Loan.LoanStatus.ACTIVE);
        if (activeLoans >= 3) {
            throw new RuntimeException("The reader already has 3 loans!");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setReader(reader);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}
