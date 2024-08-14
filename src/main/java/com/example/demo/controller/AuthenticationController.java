package com.example.demo.controller;


import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisteredUserDTO;
import com.example.demo.dto.VerifyUserDto;
import com.example.demo.model.User;
import com.example.demo.responses.LoginResponse;
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
    //postmapping post a request for a specific URL
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisteredUserDTO registeredUserDTO) {
        User registeredUser = authenticationService.signup(registeredUserDTO);
        return ResponseEntity.ok(registeredUser);
    }
    //now we are handling the postmapping for logging in
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDTO){
        //we are now grabbing the authenticated user by uding the authenticated service
        User authenticateUser = authenticationService.authenticate(loginUserDTO);
        //here we are using our methods to generate a token for the specific user
        String jwtToken = jwtService.generateToken(authenticateUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    //this is for when they actually enter the verification code
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDTO){
        try{
            authenticationService.verifyUser(verifyUserDTO);
            return ResponseEntity.ok("Account Verfied Successfully");
            //note: we created runtimeException to handle the case when a user isnt verified
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
