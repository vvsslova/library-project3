package com.github.vvsslova.libraryrest.exception.entity;

import com.github.vvsslova.libraryrest.exception.BadRequestException;

public class EntityNotUpdatedException extends BadRequestException {
    public EntityNotUpdatedException(String message) {
        super(message);
    }
}
