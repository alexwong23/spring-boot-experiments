package com.example.experiments.service;

import com.example.experiments.model.Account.User;
import com.example.experiments.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

// NOTE: Service - manipulates the data
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // find all users
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // find one user by id
    public User getUser(Long userId) {
        // NOTE: findById provided by interface
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("No user with id " + userId + " found"));

        // NOTE: orElseThrow is cleaner than !Optional.isPresent()
        // if(!userOptional.isPresent()) throw new ...

        return user;
    }

    // add new user
    public void addUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(s -> {
                    throw new IllegalStateException("Email has been taken");
                });

        // NOTE: ifPresent is cleaner than Optional.isPresent()
        // if(userOptional.isPresent()) throw new ...

        userRepository.save(user);
    }

    // update existing user
    @Transactional
    public void updateUser(Long userId, String username, String email) {
        // throw if invalid information provided
        if(username == null || username.length() < 6 || email == null || email.length() < 6)
            throw new IllegalArgumentException("Invalid username and email provided");

        // find existing user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("No user with id " + userId + " found"));

        // check if email has been taken
        userRepository.findByEmail(email).ifPresent(s -> {
            throw new IllegalStateException("Email has been taken");
        });

        // save new data
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
    }

    // delete existing user
    public void deleteUser(Long userId) {
        // NOTE: existsById provided by interface
        boolean exists = userRepository.existsById(userId);
        if(!exists)
            throw new IllegalStateException("No user with id " + userId + " found");
        userRepository.deleteById(userId);
    }
}
