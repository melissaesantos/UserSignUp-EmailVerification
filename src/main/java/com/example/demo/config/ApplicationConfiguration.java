package com.example.demo.config;


import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class ApplicationConfiguration {

    private final UserRepository userRepository;
    //constructor
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //now we will be adding 4 beans which will be injected
    @Bean
    UserDetailsService userDetailsService() {
        //we are using a lambda expression to find an email by the repository
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
