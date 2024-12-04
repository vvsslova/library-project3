package com.github.vvsslova.libraryrest.services;

import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.entity.Person;
import com.github.vvsslova.libraryrest.repositories.PersonRepository;
import com.github.vvsslova.libraryrest.util.personErrors.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final LibraryService libraryService;

    @Autowired
    public PersonService(PersonRepository personRepository, LibraryService libraryService) {
        this.personRepository = personRepository;
        this.libraryService = libraryService;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = personRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        List<Book> books = libraryService.lentBooks(findOne(id));
        for (Book book : books) {
            book.setLentPerson(null);
            book.setLendDate(null);
        }
        personRepository.deleteById(id);
    }
}
