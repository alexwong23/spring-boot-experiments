package com.example.experiments.service;

import com.example.experiments.error.exceptions.UserNotFoundException;
import com.example.experiments.model.Account.User;
import com.example.experiments.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

// NOTE: Service - perform all access to database and holds business logic of application
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // find all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // find one user by id
    public User findUserById(Long userId) {
        // NOTE: findById provided by interface
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // NOTE: orElseThrow is cleaner than !Optional.isPresent()
        // if(!userOptional.isPresent()) throw new ...

        return user;
    }

    // add new user
    public User addOneUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(s -> {
                    throw new IllegalStateException("Email has been taken");
                });

        // NOTE: ifPresent is cleaner than Optional.isPresent()
        // if(userOptional.isPresent()) throw new ...

        User newUser = userRepository.save(user);
        userRepository.flush();
        return newUser;
    }

    // update existing user
    @Transactional
    public void updateUserById(Long userId, User newUserDetails) {
        // throw if invalid information provided
        if(newUserDetails.getUsername() == null || newUserDetails.getPassword() == null || newUserDetails.getEmail() == null ||
            newUserDetails.getUsername().length() < 6  || newUserDetails.getPassword().length() < 6 || newUserDetails.getEmail().length() < 6)
            throw new IllegalArgumentException("Invalid Username, Password or Email provided");

        // find existing user by id
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // check if email has been taken
        userRepository.findByEmail(newUserDetails.getEmail())
                .filter(p -> p != existingUser)
                .ifPresent(s -> { throw new IllegalStateException("Email has been taken"); });

        // save new data
        existingUser.setUsername(newUserDetails.getUsername());
        existingUser.setPassword(newUserDetails.getPassword());
        existingUser.setEmail(newUserDetails.getEmail());
        if(newUserDetails.getFirstName() != null && !newUserDetails.getFirstName().trim().isEmpty())
            existingUser.setFirstName(newUserDetails.getFirstName());
        if(newUserDetails.getLastName() != null && !newUserDetails.getLastName().trim().isEmpty())
            existingUser.setLastName(newUserDetails.getLastName());
        userRepository.save(existingUser);
    }

    // delete existing user
    public void deleteUserById(Long userId) {
        // NOTE: existsById provided by interface
        boolean exists = userRepository.existsById(userId);
        if(!exists)
            throw new UserNotFoundException(userId);
        userRepository.deleteById(userId);
    }
}
