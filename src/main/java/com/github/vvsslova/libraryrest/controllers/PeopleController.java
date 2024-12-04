package com.github.vvsslova.libraryrest.controllers;

import com.github.vvsslova.libraryrest.dto.BookDTO;
import com.github.vvsslova.libraryrest.dto.PersonDTO;
import com.github.vvsslova.libraryrest.services.LibraryService;
import com.github.vvsslova.libraryrest.services.PersonService;
import com.github.vvsslova.libraryrest.util.hashing.IDHashing;
import com.github.vvsslova.libraryrest.util.mapper.BookMapper;
import com.github.vvsslova.libraryrest.util.mapper.PersonMapper;
import com.github.vvsslova.libraryrest.util.personErrors.PersonNotFoundException;
import com.github.vvsslova.libraryrest.util.personErrors.PersonNotSavedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/people")
public class PeopleController {
    private final PersonService personService;
    private final LibraryService libraryService;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;

    @Autowired
    public PeopleController(PersonService personService, LibraryService libraryService, PersonMapper personMapper, BookMapper bookMapper) {
        this.personService = personService;
        this.libraryService = libraryService;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
    }

    @GetMapping()
    public List<PersonDTO> getAllPeople() {
        return personService.findAll().stream().map(personMapper::convertToPersonDTO).toList();
    }

    @GetMapping("/{id}")
    public PersonDTO showPerson(@PathVariable("id") int id) {
        return personMapper.convertToPersonDTO(personService.findOne(IDHashing.toOriginalId(id)));
    }

    @GetMapping("/{id}/books")
    public List<BookDTO> showPersonsBooks(@PathVariable("id") int id) {
        return libraryService.lentBooks(personService.findOne(IDHashing.toOriginalId(id))).stream().map(bookMapper::convertToBookDTO).toList();
    }

    @PostMapping("/people")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            getBindingResult(bindingResult);
        }
        personService.save(personMapper.convertToPerson(person));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonDTO person, BindingResult bindingResult,
                                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            getBindingResult(bindingResult);
        }
        personService.update(IDHashing.toOriginalId(id), personMapper.convertToPerson(person));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        try {
            personService.findOne(IDHashing.toOriginalId(id));
        } catch (PersonNotFoundException e) {
            throw new PersonNotFoundException();
        }
        personService.delete(IDHashing.toOriginalId(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void getBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            msg.append(error.getField()).append("-").append(error.getDefaultMessage()).append(";");
        }
        throw new PersonNotSavedException(msg.toString());
    }
}

