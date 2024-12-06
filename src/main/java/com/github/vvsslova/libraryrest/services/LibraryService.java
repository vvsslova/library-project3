package com.github.vvsslova.libraryrest.services;

import com.github.vvsslova.libraryrest.dto.BookDTO;
import com.github.vvsslova.libraryrest.dto.PersonDTO;
import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.entity.Person;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotFoundException;
import com.github.vvsslova.libraryrest.mapper.book.BookMapper;
import com.github.vvsslova.libraryrest.mapper.person.PersonMapper;
import com.github.vvsslova.libraryrest.repositories.BookRepository;
import com.github.vvsslova.libraryrest.repositories.PersonRepository;
import com.github.vvsslova.libraryrest.exception.library.BookNotLentException;
import com.github.vvsslova.libraryrest.exception.library.BookNotReturnedException;
import com.github.vvsslova.libraryrest.util.hashing.IDHashing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LibraryService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final BookMapper bookMapper;
    private final PersonMapper personMapper;

    public List<BookDTO> lentBooks(int personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty()) {
            throw new EntityNotFoundException("Person not found");
        }
        return bookRepository
                .findAllByLentPersonIs(person.get())
                .stream()
                .map(bookMapper::convertToBookDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO getLentPerson(int bookId) {
        Optional<Book> fontBook = bookRepository.findById(IDHashing.hashingId(bookId));
        if (fontBook.isPresent()) {
            return personMapper.convertToPersonDTO(fontBook.get().getLentPerson());
        } else {
            throw new EntityNotFoundException("Person not found");
        }
    }

    @Transactional
    public void lendBook(int bookId, int personId) {
        Optional<Book> fontBook = bookRepository.findById(IDHashing.hashingId(bookId));
        Optional<Person> fontPerson = personRepository.findById(IDHashing.hashingId(personId));
        if (fontBook.isPresent() & fontPerson.isPresent()) {
            Book lendingBook = fontBook.get();
            lendingBook.setLentPerson(fontPerson.get());
            lendingBook.setLendDate(new Date());
            bookService.update(bookId, bookMapper.convertToBookDTO(lendingBook));
        } else {
            throw new BookNotLentException("Book don't lent");
        }
    }

    @Transactional
    public void returnBook(int bookId) {
        Optional<Book> fontBook = bookRepository.findById(IDHashing.hashingId(bookId));
        if (fontBook.isPresent()) {
            fontBook.get().setLentPerson(null);
            fontBook.get().setLendDate(null);
        } else {
            throw new BookNotReturnedException("Book don't returned");
        }
    }

    public void setNullPerson(int personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty()) {
            throw new EntityNotFoundException("Person not found");
        }
        List<Book> books = bookRepository.findAllByLentPersonIs(person.get());
        for (Book book : books) {
            book.setLentPerson(null);
            book.setLendDate(null);
        }
    }
}

