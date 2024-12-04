package com.github.vvsslova.libraryrest.util.libraryErrors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LibraryExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<LibraryErrorResponse> handleException(BookNotLentException e) {
        LibraryErrorResponse response = new LibraryErrorResponse("Book don't lent");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<LibraryErrorResponse> handleException(BookNotReturnedException e) {
        LibraryErrorResponse response = new LibraryErrorResponse("Book don't returned");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
