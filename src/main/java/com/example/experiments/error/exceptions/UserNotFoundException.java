package com.example.experiments.error.exceptions;

// Custom exception - for UserController to render custom errors
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User id not found: " + id);
    }
}
