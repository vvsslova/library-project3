package com.github.vvsslova.libraryrest.util.bookErrors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<BookErrorResponse> handleException(BookNotFoundException e) {
        BookErrorResponse response = new BookErrorResponse("Book not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<BookErrorResponse> handleException(BookNotSavedException e) {
        BookErrorResponse response = new BookErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
