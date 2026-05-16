package com.thegoodsamaritan.person.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException() {
        super("person not found");
    }
}
