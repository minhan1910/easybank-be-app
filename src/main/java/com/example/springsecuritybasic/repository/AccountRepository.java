package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByCustomerId(Integer customerId);
}
