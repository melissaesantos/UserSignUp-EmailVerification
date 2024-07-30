package com.example.demo.service;
import lombok.Value;
import  org.springframework.stereotype.Value;
import org.springframework.stereotype.Service;
//the spring service component allows it to be automatically discovered and instantiated
@Service
public class JwtService {
    //this is taking the value from our security.jw.secretkey in our application.properties
    @value("${security.jwt.secret.key}")
    private String secretKey;

    @value("${security.jwt.expiration-time}")
    private long jwtExpiration;
}
