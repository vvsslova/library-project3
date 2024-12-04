package com.github.vvsslova.libraryrest.util.personErrors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PeopleExceptionHandler {
    @ExceptionHandler
    private ResponseEntity<PeopleErrorResponse> handleException(PersonNotFoundException e) {
        PeopleErrorResponse response = new PeopleErrorResponse("Person not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PeopleErrorResponse> handleException(PersonNotSavedException e) {
        PeopleErrorResponse response = new PeopleErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}