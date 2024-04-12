package com.example.springsecuritybasic.controller;

import com.example.springsecuritybasic.model.Card;
import com.example.springsecuritybasic.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/myCards")
    public List<Card> getCard(@RequestParam Integer id) {
        return cardRepository.findByCustomerId(id);
    }
}

