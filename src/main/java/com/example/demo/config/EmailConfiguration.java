package com.example.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {
    @Value("${spring.mail.username}")
    private String emailUsername;
    @Value("${spring.mail.password}")
    private String password;


    @Bean
    //this is how we are gonna send the mail from spring
    public JavaMailSender javaMailSender() {
        //now we are creating the javaMailSender object
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();


    }
}
