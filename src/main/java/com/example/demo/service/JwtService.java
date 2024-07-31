package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
//Imports utilities for generating cryptographic keys.
import io.jsonwebtoken.security.Keys;
// Imports the Key interface, representing a cryptographic key
import java.security.Key;
//Allows injection of values from the application properties file.
import org.springframework.beans.factory.annotation.Value;
//import sthe 'User' c;ass representing the user's detail
import org.springframework.security.core.userdetails.User;
//imports the 'UserDetail' interface, representing a user's core info
import org.springframework.security.core.userdetails.UserDetails;
//Marks the class as a service component in the Spring framework
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
//@service: spring will auto detect this class and manage it as a Spring bean, making it available to inject
//in other classses
//@service indicates that this class is a service component, meaning it contains business logic and can be injected into other components
@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    //^ tells spring to find the security.jwt.secret-key property in the application properties file
    //and inject its value into the secretKey variable
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        //^extracts a specific claim from the JWT
        final Claims claims = extractAllClaims(token);
        //^extracts all claims from the JWT
        //the 'apply' method from the Function interface takes the 'Claims' object as input and processes it to return a
        //value of type 'T'
        return claimsResolver.apply(claims);
    }
    //this is generating a JWT token from the users detail object w/o additional claims
    public String generateToken(UserDetails userDetails){
        //calls the overloaded 'generateToken' method with an empty map for additional claims
        return generateToken(new HashMap<>(), userDetails);
    }
    //generates a JWT for the given user details and additional claims
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        //calls tje 'buildToken' method to construct the JWT
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public long getExpirationTime(){
        return jwtExpiration;
    }
//COnstructs the JWT using the provided claims, user details, and expiration time
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        //this is the webtoken
        return Jwts
                //begins building the JWT
                .builder()
                //sets additional claims for the JWT
                .setClaims(extraClaims)
                //sets the subject(username) of the JWT
                .setSubject(userDetails.getUsername())
                //setting the issue date and assigning it to token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //sets the expiration time of the JWT
                .setExpiration(new Date(System.currentTimeMillis()+ expiration))
                //sign the JWT wit hthe secret key and ES256 algo
                .signWith(getSignInKey(),SignatureAlgorithm.ES256)
                //cOMPACTING THE jwt function
                .compact();
    }

//checks if the JWT is valid for the given user details
    public boolean isTokenValid(String token, UserDetails userDetails) {
        //extracts the username from the token
        final String username = extractUsername(token);
        //returns true if the username matches and the token is not expired
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));

    }
    //checks if JWT has expired
    private boolean isTokenExpired(String token){
        //returns tru if the expiration date is before the current date
        return extractExpiration(token).before(new Date());
    }
    //extracts the expiration from the JWT
    private Date extractExpiration(String token){
        //calls 'extractClaim' with a function to get the expiration date
        return extractClaim(token, Claims::getExpiration);
    }
//extracts all claims from the JWT
    private Claims extractAllClaims(String token){
        //this is parsing and retrieving all of our claims from the JWT token
        return Jwts
                //begins parsing the JWT
                .parserBuilder()
                //sets the key used to sign the JWT
                .setSigningKey(getSignInKey())
                .build()
                //parses the token and returns the claims
                .parseClaimsJws(token)
                .getBody();

    }
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}