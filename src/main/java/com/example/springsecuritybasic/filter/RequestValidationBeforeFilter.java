package com.example.springsecuritybasic.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {

    public static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
    private Charset credentialsCharset = StandardCharsets.UTF_8;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        String header = ((HttpServletRequest) request).getHeader("Authorization");
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if (header == null) {
            return;
        } else {
            header = header.trim();
            if (!StringUtils.startsWithIgnoreCase(header, "Basic")) {
                return;
            } else if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BASIC)) {
                throw new BadCredentialsException("Empty basic authentication token");
            } else {
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decoded = this.decode(base64Token);
                String token = new String(decoded, this.credentialsCharset);
                int delim = token.indexOf(":");
                if (delim == -1) {
                    throw new BadCredentialsException("Invalid basic authentication token");
                }
                String email = token.substring(0, delim);
                if (email.toLowerCase().contains("test")) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }

        // next
        filterChain.doFilter(request, servletResponse);
    }

    private byte[] decode(byte[] base64Token) {
        try {
            return Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var3) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
    }
}

