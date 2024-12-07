package com.github.vvsslova.libraryrest.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class BookFilter {
    private String titleStartsWith;
    private String sortBy;
    private int page;
    private int size;

    public BookFilter() {
        this.page = 0;
        this.size = 10;
    }
}

