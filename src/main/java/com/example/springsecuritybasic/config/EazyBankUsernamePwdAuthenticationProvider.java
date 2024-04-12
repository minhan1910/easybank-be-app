package com.example.springsecuritybasic.config;

import com.example.springsecuritybasic.model.Authority;
import com.example.springsecuritybasic.model.Customer;
import com.example.springsecuritybasic.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EazyBankUsernamePwdAuthenticationProvider(CustomerRepository customerRepository,
                                                     PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<Customer> customer = customerRepository.findByEmail(username);

        if (customer.isEmpty()) {
            throw new BadCredentialsException("No user registered with this details");
        }

        Customer customerFromDb = customer.get(0);

        if (!passwordEncoder.matches(pwd, customerFromDb.getPwd())) {
            throw new BadCredentialsException("Email or password incorrect!");
        }

//        List<GrantedAuthority> authorities =
//                List.of(new SimpleGrantedAuthority(customerFromDb.getRole()));

        return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthority(customerFromDb.getAuthorities()));
    }

    private List<GrantedAuthority> getGrantedAuthority(Set<Authority> authorities) {
        return authorities
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
