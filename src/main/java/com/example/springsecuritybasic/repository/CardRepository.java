package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.model.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long> {
    List<Card> findByCustomerId(int customerId);
}
