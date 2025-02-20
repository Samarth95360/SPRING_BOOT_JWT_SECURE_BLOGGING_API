package com.example.Blogging.ExceptionHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        if(authException instanceof BadCredentialsException){

            response.getWriter().write(authException.getMessage());

        }else if(authException instanceof UsernameNotFoundException){

            response.getWriter().write(authException.getMessage());

        }else if(authException instanceof AccountExpiredException){

            response.getWriter().write(authException.getMessage());

        }else if(authException instanceof InsufficientAuthenticationException){

            response.getWriter().write(authException.getMessage());

        }else {
            response.getWriter().write("Authentication failed: " + authException.getMessage());
        }

    }
}
