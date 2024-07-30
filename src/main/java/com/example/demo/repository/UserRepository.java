package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends CrudRepository<User, Long> {
    //did optional since it doesn't guarantee that we will find a user.
    //now we can find the user by their email
    Optional<User> findByEmail(String email);
    //we will now be doing the same thing but by verification code
    //this is to verify they are entering the correct verif code
    Optional<User> findByVerificatonCode(String verificationCode);
}
