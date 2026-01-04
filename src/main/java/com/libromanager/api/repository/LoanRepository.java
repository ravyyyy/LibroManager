package com.libromanager.api.repository;

import com.libromanager.api.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByReaderIdAndStatus(Long readerId, Loan.LoanStatus status);
}
