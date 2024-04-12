package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.model.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Long> {

}
