package com.github.vvsslova.libraryrest.controllers;

import com.github.vvsslova.libraryrest.dto.PersonDTO;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotSavedException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotUpdatedException;
import com.github.vvsslova.libraryrest.services.LibraryService;
import com.github.vvsslova.libraryrest.services.MessageService;
import com.github.vvsslova.libraryrest.services.PersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/people")
public class PeopleController {
    private final PersonService personService;
    private final LibraryService libraryService;
    private final MessageService messageService;

    @GetMapping()
    public List<PersonDTO> getAllPeople() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public PersonDTO showPerson(@PathVariable("id") int id) {
        return personService.findPerson(id);
    }

    @PostMapping()
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid PersonDTO person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityNotSavedException(messageService.getBindingResult(bindingResult));
        }
        PersonDTO savedPerson = personService.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> update(@RequestBody @Valid PersonDTO person, BindingResult bindingResult,
                                            @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            throw new EntityNotUpdatedException(messageService.getBindingResult(bindingResult));
        }
        personService.update(id, person);
        PersonDTO updatedPerson = personService.update(id, person);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        personService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{book_id}")
    public PersonDTO getPersonByBookID(@PathVariable int book_id) {
        return libraryService.getLentPerson(book_id);
    }
}

