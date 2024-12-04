package com.github.vvsslova.libraryrest.util.bookErrors;

public class BookNotSavedException extends RuntimeException {
    public BookNotSavedException(String message) {
        super(message);
    }
}
