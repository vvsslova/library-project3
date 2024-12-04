package com.github.vvsslova.libraryrest.util.personErrors;

public class PersonNotSavedException extends RuntimeException {
    public PersonNotSavedException(String message) {
        super(message);
    }
}
