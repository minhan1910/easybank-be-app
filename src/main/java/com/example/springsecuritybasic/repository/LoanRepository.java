package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.model.Loan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {
    List<Loan> findByCustomerIdOrderByStartDtDesc(Integer customerId);
}
