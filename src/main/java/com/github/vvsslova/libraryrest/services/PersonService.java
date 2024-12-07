package com.github.vvsslova.libraryrest.services;

import com.github.vvsslova.libraryrest.dto.PersonDTO;
import com.github.vvsslova.libraryrest.entity.Person;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotDeletedException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotFoundException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotSavedException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotUpdatedException;
import com.github.vvsslova.libraryrest.mapper.person.PersonMapper;
import com.github.vvsslova.libraryrest.repositories.PersonRepository;
import com.github.vvsslova.libraryrest.util.hashing.IDHashing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final LibraryService libraryService;
    private final PersonMapper personMapper;

    public List<PersonDTO> findAll() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::convertToPersonDTO)
                .toList();
    }

    public PersonDTO findPerson(int id) {
        Optional<Person> foundPerson = personRepository.findById(IDHashing.hashingId(id));
        return foundPerson
                .map(personMapper::convertToPersonDTO)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
    }

    @Transactional
    public PersonDTO save(PersonDTO person) {
        try {
            Person convertedPerson = personMapper.convertToPerson(person);
            personRepository.save(convertedPerson);
            return personMapper.convertToPersonDTO(convertedPerson);
        } catch (Exception e) {
            throw new EntityNotSavedException("Person not saved");
        }
    }

    @Transactional
    public PersonDTO update(int id, PersonDTO updatedPerson) {
        try {
            Person convertedPerson = personMapper.convertToPerson(updatedPerson);
            convertedPerson.setId(IDHashing.hashingId(id));
            personRepository.save(convertedPerson);
            return personMapper.convertToPersonDTO(convertedPerson);
        } catch (Exception e) {
            throw new EntityNotUpdatedException("Person not updated");
        }
    }

    @Transactional
    public void delete(int id) {
        try {
            libraryService.setNullPerson(IDHashing.hashingId(id));
            personRepository.deleteById(IDHashing.hashingId(id));
        } catch (Exception e) {
            throw new EntityNotDeletedException("Person not deleted");
        }
    }
}
