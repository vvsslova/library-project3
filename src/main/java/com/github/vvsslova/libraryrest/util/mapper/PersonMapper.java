package com.github.vvsslova.libraryrest.util.mapper;

import com.github.vvsslova.libraryrest.dto.PersonDTO;
import com.github.vvsslova.libraryrest.entity.Person;
import com.github.vvsslova.libraryrest.util.hashing.IDHashing;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    private final ModelMapper modelMapper;

    public PersonMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PersonDTO convertToPersonDTO(Person person) {
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);
        if (person.getId() == 0) {
            return personDTO;
        }
        personDTO.setId(IDHashing.toHashedId(person.getId()));
        return personDTO;
    }

    public Person convertToPerson(PersonDTO personDTO) {
        Person person = modelMapper.map(personDTO, Person.class);
        if (personDTO.getId() == 0) {
            return person;
        }
        person.setId(IDHashing.toOriginalId(personDTO.getId()));
        return person;
    }
}
