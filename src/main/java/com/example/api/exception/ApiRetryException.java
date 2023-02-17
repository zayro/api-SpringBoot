package com.example.api.exception;

public class ApiRetryException extends RuntimeException {
    public ApiRetryException(String message) {
        super(message);
    }
}