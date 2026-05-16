package com.thegoodsamaritan.person.exception;

public class EmailConflictException extends RuntimeException {

    public EmailConflictException() {
        super("email already in use");
    }
}
