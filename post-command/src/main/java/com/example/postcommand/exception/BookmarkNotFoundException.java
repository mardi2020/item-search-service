package com.example.postcommand.exception;

public class BookmarkNotFoundException extends RuntimeException {

    private final static String MSG = "BOOKMARK_NOT_FOUND";

    public BookmarkNotFoundException() {
        super(MSG);
    }
}
