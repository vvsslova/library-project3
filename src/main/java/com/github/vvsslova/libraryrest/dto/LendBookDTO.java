package com.github.vvsslova.libraryrest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LendBookDTO {
    private int bookId;
    private int userId;
}
