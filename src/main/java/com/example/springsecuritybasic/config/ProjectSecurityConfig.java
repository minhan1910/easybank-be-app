package com.example.springsecuritybasic.config;

import com.example.springsecuritybasic.filter.AuthoritiesLoggingAfterFilter;
import com.example.springsecuritybasic.filter.AuthoritiesLoggingAtFilter;
import com.example.springsecuritybasic.filter.CsrfCookieFilter;
import com.example.springsecuritybasic.filter.RequestValidationBeforeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {
    private final String[] permitAllEndpoints =
            {
                    "/notices",
                    "/contact",
                    "/register"
            };

    private final String[] authenticatedEndpoints =
            {
                    "/myAccount",
                    "/myBalance",
                    "/myLoans",
                    "/myCards",
                    "/user"
            };

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http.securityContext((context) -> context
                        .requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            });
        });

        http.csrf(httpSecurityCsrfConfigurer ->
                        httpSecurityCsrfConfigurer
                                .csrfTokenRequestHandler(requestHandler)
                                .ignoringRequestMatchers("/contact", "/register")
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);

        http.authorizeHttpRequests((requests) -> requests
//                .requestMatchers(authenticatedEndpoints).authenticated()
                        .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                        .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                        .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                        .requestMatchers("/myBalance").hasAuthority("VIEWBALANCE")
                        .requestMatchers("/user").authenticated()
                        .requestMatchers(permitAllEndpoints).permitAll()
                        .anyRequest().authenticated()
        );

        http.formLogin(withDefaults());

        http.httpBasic(withDefaults());

        return http.build();
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user1 = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .authorities("admin")
//                .build();
//
//        UserDetails user2 = User.withDefaultPasswordEncoder()
//                .username("guest")
//                .password("guest")
//                .authorities("guest")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // plain text
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    Config order of multiple authentication providers:
//    Please note if two or more AuthenticationProviders return true for a given Authentication
//    object, the AuthenticationManager will use the first AuthenticationProvider that returned true. This is because the AuthenticationManager will only continue to invoke AuthenticationProviders if the previous AuthenticationProvider returned false.

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        return authenticationManagerBuilder.authenticationProvider(myFirstProvider)
//                .authenticationProvider(mySecondProvider).build();
//    }

}
