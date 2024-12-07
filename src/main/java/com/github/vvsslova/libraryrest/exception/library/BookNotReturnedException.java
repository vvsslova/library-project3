package com.github.vvsslova.libraryrest.exception.library;

import com.github.vvsslova.libraryrest.exception.BadRequestException;

public class BookNotReturnedException extends BadRequestException {
    public BookNotReturnedException(String message) {
        super(message);
    }
}
