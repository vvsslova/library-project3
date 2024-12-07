package com.github.vvsslova.libraryrest.exception.library;

import com.github.vvsslova.libraryrest.exception.BadRequestException;

public class BookNotLentException extends BadRequestException {
    public BookNotLentException(String message) {
        super(message);
    }
}
