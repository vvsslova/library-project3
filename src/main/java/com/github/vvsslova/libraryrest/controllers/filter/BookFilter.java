package com.github.vvsslova.libraryrest.controllers.filter;

import lombok.Getter;
import lombok.Setter;

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
