package com.example.postquery.exception;

public class HashtagNotFoundException extends RuntimeException {

    private static final String MESSAGE = "HASHTAG_NOT_FOUND";

    public HashtagNotFoundException() {
        super(MESSAGE);
    }
}
