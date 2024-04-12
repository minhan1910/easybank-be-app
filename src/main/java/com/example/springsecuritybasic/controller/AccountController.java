package com.example.springsecuritybasic.controller;

import com.example.springsecuritybasic.model.Account;
import com.example.springsecuritybasic.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/myAccount")
    public Account getAccountDao(@RequestParam Integer id) {
        Account account = accountRepository.findByCustomerId(id);
        return account;
    }

}
