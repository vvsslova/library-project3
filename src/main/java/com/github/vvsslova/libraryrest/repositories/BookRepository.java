package com.github.vvsslova.libraryrest.repositories;

import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByLentPersonIs(Person lentPerson);

    List<Book> findByTitleStartingWith (String title);
}
