package com.github.vvsslova.libraryrest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "people")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 5, max = 70, message = "Name should be between 2 and 60 characters!")
    @Pattern(regexp = "^[А-Я][а-я]+\\s[А-Я][а-я]+\\s[А-Я][а-я]+$",
            message = "Введите данные в корректном формате")
    @Column(name = "name")
    private String name;

    @Min(value = 1900, message = "Year of birth should not be smaller than 1900!")
    @Max(value = 2024, message = "Year of birth should not be grater than 2024!")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "lentPerson")
    private List<Book> lentBooks;
}
