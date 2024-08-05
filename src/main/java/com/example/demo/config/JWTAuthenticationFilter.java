package com.example.demo.config;


import com.example.demo.service.JwtService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

//**
// @component is marking this class as a spring component which allows it to be automactically
//detected and instantiated by springs component scanning
// */
@Component
public class JWTAuthenticationFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    //now we will define a constructor to inject these dependencies and initialize the instance variables
    public JWTAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver, JwtService jwtService, UserDetailsService userDetailsService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }




}
