package com.github.vvsslova.libraryrest.exception.entity;

import com.github.vvsslova.libraryrest.exception.BadRequestException;

public class EntityNotDeletedException extends BadRequestException {
  public EntityNotDeletedException(String message) {
    super(message);
  }
}
