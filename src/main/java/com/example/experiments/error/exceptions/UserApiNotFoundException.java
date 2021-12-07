package com.example.experiments.error.exceptions;

// Custom exception - for UserApiController to throw custom JSON errors
public class UserApiNotFoundException extends RuntimeException {
    public UserApiNotFoundException(Long id) {
        super("User id not found: " + id);
    }
}
