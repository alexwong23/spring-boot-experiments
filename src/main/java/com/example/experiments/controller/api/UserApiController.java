package com.example.experiments.controller.api;

import com.example.experiments.error.exceptions.UserApiException;
import com.example.experiments.error.exceptions.UserApiNotFoundException;
import com.example.experiments.model.Account.User;
import com.example.experiments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserApiController {

    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping(path = "{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        try {
            return userService.findUserById(userId);
        } catch(Exception ex) {
            throw new UserApiNotFoundException(userId); // NOTE: Catch and throw custom JSON error
        }
    }

    @PostMapping
    public ResponseEntity<User> createNewUser(@ModelAttribute User newUser) {
        try {
            newUser.setDob(LocalDate.now()); // TODO: DOB value should be provided in form
            User savedUser = userService.addOneUser(newUser);
            return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
        } catch(Exception ex) {
            throw new UserApiException(ex.getMessage()); // NOTE: Catch and throw custom JSON error
        }
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,
                             @ModelAttribute User updateUser) {
        try {
            updateUser.setDob(LocalDate.now()); // TODO: DOB value should be provided in form
            User savedUser = userService.updateUserById(userId, updateUser);
            return new ResponseEntity<User>(savedUser, HttpStatus.OK);
        } catch(Exception ex) {
            throw new UserApiException(ex.getMessage()); // NOTE: Catch and throw custom JSON error
        }
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUserById(userId);
            return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
        } catch(Exception ex) {
            throw new UserApiNotFoundException(userId); // NOTE: Catch and throw custom JSON error
        }
    }
}
