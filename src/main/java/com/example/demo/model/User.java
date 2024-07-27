package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// here we are just making the user fields
@Entity
@Table(name = "user")
@Getter
@Setter
public class User  implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    //password does not need to be unique
    @Column(nullable = false)
    private String password;

    //build the constructorsfor all of the fields along with their getter nd setter
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(){}
//this overriding to meet the requirement to meet the details of the user interface
    //returning empty list since we are not doing role based authentication
    //*************WE COULD DO ROLE BASED AUTHENTICATION FOR THIS *******************
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
