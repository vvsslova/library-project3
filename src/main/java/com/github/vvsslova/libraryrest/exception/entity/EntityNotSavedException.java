package com.github.vvsslova.libraryrest.exception.entity;

import com.github.vvsslova.libraryrest.exception.BadRequestException;

public class EntityNotSavedException extends BadRequestException {
  public EntityNotSavedException(String message) {
    super(message);
  }
}
