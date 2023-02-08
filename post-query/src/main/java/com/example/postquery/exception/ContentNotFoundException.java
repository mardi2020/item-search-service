package com.example.postquery.exception;

public class ContentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "NOT_FOUND_CONTENT";

    public ContentNotFoundException() {
        super(MESSAGE);
    }
}
