package com.example.postquery.exception;

public class BookmarkNotFoundException extends RuntimeException {

    private static final String MESSAGE = "NOT_FOUND_BOOKMARK";

    public BookmarkNotFoundException() {
        super(MESSAGE);
    }
}
