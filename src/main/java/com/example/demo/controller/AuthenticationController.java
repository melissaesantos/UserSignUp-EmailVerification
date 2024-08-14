package com.example.demo.controller;


import com.example.demo.dto.RegisteredUserDTO;
import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;

    }
//people can go ahead and create accounts when they sign up
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisteredUserDTO registeredUserDTO) {
        User registeredUser = authenticationService.signup(registeredUserDTO);
        return ResponseEntity.ok(registeredUser);
    }
}
