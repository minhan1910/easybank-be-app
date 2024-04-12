package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.model.AccountTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long> {
    List<AccountTransaction> findByCustomerIdOrderByTransactionDtDesc(Integer customerId);
}
