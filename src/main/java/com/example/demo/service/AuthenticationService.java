package com.example.demo.service;
//this contains the logic for both authenticating existing users and creating
//new users

import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisteredUserDTO;
import com.example.demo.dto.VerifyUserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
            throw new RuntimeException("Account not verified. Please verify your email :)");
        }
        //now we are authenticating the user
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(input.getEmail(),
                        input.getPassword()));
        return user;
    }

    public void verifyUser(VerifyUserDto input){
        //user might not exist so it'll be optional
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

        if(optionalUser.isPresent()){
            //this means that we were able to find a user so nowe we can go verify them
            User user = optionalUser.get();
            //here we are checking if their verification code already expired
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification Code has Expired");
            }
            if(user.getVerificationCode().equals(input.getVerifyCode())){
                //now we are enabling them if their verif code is valid
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            }else{
                //now if they enter the incorrect verification code
                throw new RuntimeException("Invalid verification code");
            }

        }else{
            throw new RuntimeException("User not found");
        }
    }
    //now lets work on resending the verification code if it expires or if they type it in wrong
    public void resendVerificationEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.isEnabled()){
                throw new RuntimeException("account is already verified");
            }
            user.setVerificationCode(generateVerificcationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(5));
            SendVerificationEmail(user);
            userRepository.save(user);

        }else{
            //if user is not found in our repository
            throw new RuntimeException("User not found");
        }

    }

    public void SendVerificationEmail(User user){
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE" + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerifcationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }



}
