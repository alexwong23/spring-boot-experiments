package com.example.experiments.error.exceptions;

public class UserApiNotFoundException extends RuntimeException {
    public UserApiNotFoundException(Long id) {
        super("User id not found: " + id);
    }
}
