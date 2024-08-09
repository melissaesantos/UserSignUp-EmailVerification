package com.example.demo.service;
//this contains the logic for both authenticating existing users and creating
//new users

import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisteredUserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    public AuthenticationService (
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signup(RegisteredUserDTO input){
        //we use the password encoder that way the password stays hidden
        User user = new User(input.getUserName(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        //we are gonna set them to not be enabled because they havent verified their email yet
        user.setEnabled(false);
        //now this is sending that verification email
        sendVerificationEmail(user);
        //now this is saving the user
        return userRepository.save(user);

    }

    //now we are working on the actual authentication
    public User authenticate(LoginUserDto input){
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        //so right now we are taking care of the case in which they login but they have not verified their account
        if(!user.isEnabled()){
            throw new RuntimeException("User is not enabled");
        }
    }

}
