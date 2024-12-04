package com.github.vvsslova.libraryrest.services;

import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.entity.Person;
import com.github.vvsslova.libraryrest.repositories.BookRepository;
import com.github.vvsslova.libraryrest.repositories.PersonRepository;
import com.github.vvsslova.libraryrest.util.libraryErrors.BookNotLentException;
import com.github.vvsslova.libraryrest.util.libraryErrors.BookNotReturnedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LibraryService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public LibraryService(PersonRepository personRepository, BookRepository bookRepository, BookService bookService) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    public List<Book> lentBooks(Person person) {
        return bookRepository.findAllByLentPersonIs(person);
    }

    public Person getLentPerson(int id) {
        Optional<Book> fontBook = bookRepository.findById(id);
        return fontBook.map(Book::getLentPerson).orElse(null);
    }

    @Transactional
    public void lendBook(int bookId, int personId) {
        Optional<Book> fontBook = bookRepository.findById(bookId);
        Optional<Person> fontPerson = personRepository.findById(personId);
        if (fontBook.isPresent() & fontPerson.isPresent()) {
            Book lendingBook = fontBook.get();
            lendingBook.setLentPerson(fontPerson.get());
            lendingBook.setLendDate(new Date());
            bookService.update(bookId, lendingBook);
        } else {
            throw new BookNotLentException();
        }
    }

    @Transactional
    public void returnBook(int bookId) {
        Optional<Book> fontBook = bookRepository.findById(bookId);
        if (fontBook.isPresent()) {
            fontBook.get().setLentPerson(null);
            fontBook.get().setLendDate(null);
        } else {
            throw new BookNotReturnedException();
        }
    }
}

