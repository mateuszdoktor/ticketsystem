package com.example.ticketsystem.exceptions.user;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
