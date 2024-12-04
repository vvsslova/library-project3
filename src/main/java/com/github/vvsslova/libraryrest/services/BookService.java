package com.github.vvsslova.libraryrest.services;

import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.repositories.BookRepository;
import com.github.vvsslova.libraryrest.util.bookErrors.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(Sort var) {
        return bookRepository.findAll(var);
    }

    public Page<Book> findAll(Pageable var) {
        return bookRepository.findAll(var);
    }

    public Book findOne(int id) {
        Optional<Book> fontBook = bookRepository.findById(id);
        return fontBook.orElseThrow(BookNotFoundException::new);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book oldBook = findOne(id);
        updatedBook.setId(id);
        updatedBook.setLentPerson(oldBook.getLentPerson());
        updatedBook.setLendDate(oldBook.getLendDate());
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchBook(String title) {
        return bookRepository.findByTitleStartingWith(title);
    }
}
