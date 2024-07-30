package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
//the spring service component allows it to be automatically discovered and instantiated

@Service
public class JwtService {
    //this is taking the value from our security.jw.secretkey in our application.properties
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;
    //this is extracting the username form our JWT token
    public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
    }
}
