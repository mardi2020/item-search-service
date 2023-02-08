package com.example.postquery.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String MESSAGE = "USER_NOT_FOUND";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
