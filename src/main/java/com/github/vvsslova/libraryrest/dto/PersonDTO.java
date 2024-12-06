package com.github.vvsslova.libraryrest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PersonDTO {

    private int id;

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 5, max = 70, message = "Name should be between 2 and 60 characters!")
    @Pattern(regexp = "^[А-Я][а-я]+\\s[А-Я][а-я]+\\s[А-Я][а-я]+$",
            message = "Введите данные в корректном формате")
    private String name;

    @Min(value = 1900, message = "Year of birth should not be smaller than 1900!")
    @Max(value = 2024, message = "Year of birth should not be grater than 2024!")
    private int yearOfBirth;
}
