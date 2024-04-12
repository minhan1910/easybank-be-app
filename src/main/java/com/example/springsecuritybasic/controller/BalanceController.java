package com.example.springsecuritybasic.controller;

import com.example.springsecuritybasic.model.AccountTransaction;
import com.example.springsecuritybasic.repository.AccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @GetMapping("/myBalance")
    public List<AccountTransaction> getBalanceDetails(@RequestParam Integer id) {
        List<AccountTransaction> transactions = accountTransactionRepository.findByCustomerIdOrderByTransactionDtDesc(id);
        return transactions;
    }
}
