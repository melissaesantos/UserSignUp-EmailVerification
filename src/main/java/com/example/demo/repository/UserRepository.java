package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends CrudRepository<User, Long> {
    //did optional since it doesnt guarantee that we will find a user.
    //now we can find the user by their email
    Optional<User> findByEmail(String email);

}
