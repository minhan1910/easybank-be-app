package com.example.springsecuritybasic.config;

import com.example.springsecuritybasic.model.Customer;
import com.example.springsecuritybasic.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EazyBankUserDetails implements UserDetailsService {

    private CustomerRepository customerRepository;

    @Autowired
    public EazyBankUserDetails(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username;
        String userName, password = null;
        List<GrantedAuthority> authorities = null;
        List<Customer> customer = customerRepository.findByEmail(email);

        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User details not found for the user: " + email);
        }

        userName = customer.get(0).getEmail();
        password = customer.get(0).getPwd();
        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));

        return new User(userName, password, authorities);
    }

}
