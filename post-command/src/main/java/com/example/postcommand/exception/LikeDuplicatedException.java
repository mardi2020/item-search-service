package com.example.postcommand.exception;

public class LikeDuplicatedException extends RuntimeException {

    private static final String MESSAGE = "LIKE_DUPLICATED";

    public LikeDuplicatedException() {
        super(MESSAGE);
    }
}
