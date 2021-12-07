package com.example.experiments.error.exceptions;

public class UserApiException extends RuntimeException {
    public UserApiException(String message) {
        super(message);
    }
}
