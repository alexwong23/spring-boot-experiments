package com.example.experiments.error.exceptions;

// Custom exception - for UserApiController to throw custom JSON errors
public class UserApiException extends RuntimeException {
    public UserApiException(String message) {
        super(message);
    }
}
