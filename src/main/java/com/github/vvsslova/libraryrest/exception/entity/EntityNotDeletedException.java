package com.github.vvsslova.libraryrest.exception.entity;

public class EntityNotDeletedException extends RuntimeException {
  public EntityNotDeletedException(String message) {
    super(message);
  }
}
