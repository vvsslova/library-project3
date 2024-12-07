package com.github.vvsslova.libraryrest.controllers;

import com.github.vvsslova.libraryrest.dto.BookDTO;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotSavedException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotUpdatedException;
import com.github.vvsslova.libraryrest.services.BookService;
import com.github.vvsslova.libraryrest.services.LibraryService;
import com.github.vvsslova.libraryrest.services.MessageService;
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
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final LibraryService libraryService;
    private final MessageService messageService;

    @GetMapping()
    public ResponseEntity<List<BookDTO>> getAllBooks(BookFilter bookFilter) {
        List<BookDTO> books = bookService.findAll(bookFilter);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public BookDTO showBook(@PathVariable("id") int id) {
        return bookService.findBook((id));
    }

    @GetMapping("/{id}/delay_result")
    public boolean getDelayResult(@PathVariable("id") int id) {
        return bookService.getDelayResult(id);
    }

    @PostMapping()
    public ResponseEntity<BookDTO> create(@RequestBody @Valid BookDTO book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityNotSavedException(messageService.getBindingResult(bindingResult));
        }
        BookDTO savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@RequestBody @Valid BookDTO book,
                                          BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            throw new EntityNotUpdatedException(messageService.getBindingResult(bindingResult));
        }
        BookDTO updatedBook = bookService.update(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<HttpStatus> returnBook(@PathVariable("id") int id) {
        libraryService.returnBook(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/lend/{person_id}/{book_id}")
    public ResponseEntity<HttpStatus> lendBook(@PathVariable("person_id") int person_id, @PathVariable("book_id") int book_id) {
        libraryService.lendBook(book_id, person_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{person_id}")
    public List<BookDTO> showPersonsBooks(@PathVariable int person_id) {
        return libraryService.lentBooks(person_id);
    }
}
