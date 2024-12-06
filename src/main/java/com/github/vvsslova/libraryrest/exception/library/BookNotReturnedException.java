package com.github.vvsslova.libraryrest.exception.library;

public class BookNotReturnedException extends RuntimeException {
    public BookNotReturnedException(String message) {
        super(message);
    }
}
