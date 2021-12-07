package com.example.experiments.error.exceptions;

public class ApiUserNotFoundException extends RuntimeException {
    public ApiUserNotFoundException(Long id) {
        super("User id not found: " + id);
    }
}
