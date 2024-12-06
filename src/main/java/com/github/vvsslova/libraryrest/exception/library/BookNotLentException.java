package com.github.vvsslova.libraryrest.exception.library;

public class BookNotLentException extends RuntimeException {
    public BookNotLentException(String message) {
        super(message);
    }
}
