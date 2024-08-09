package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    //this is actually gonna send the mail for us
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerifcationEmail(String to, String subject, String content) throws MessagingException{
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //true allows there to be html/css so it helps when we want to design the verification itself
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        //here the 2nd paramater states whether it was written in html
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }

}
