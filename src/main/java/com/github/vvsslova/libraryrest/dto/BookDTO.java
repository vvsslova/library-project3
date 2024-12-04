package com.github.vvsslova.libraryrest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class BookDTO {

    private int id;

    @NotEmpty(message = "Title should not be empty!")
    @Size(min = 5, max = 70, message = "Title should be between 2 and 40 characters!")
    private String title;

    @NotEmpty(message = "Author should not be empty!")
    @Size(min = 5, max = 70, message = "Author should be between 2 and 40 characters!")
    private String author;

    @Max(value = 2024, message = "Year of publication should not be grater than 2024!")
    private int yearOfPublication;

    public BookDTO(String title, String author, int yearOfPublication) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }
}
