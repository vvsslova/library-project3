package com.github.vvsslova.libraryrest.exception.entity;

public class EntityNotSavedException extends RuntimeException {
  public EntityNotSavedException(String message) {
    super(message);
  }
}
