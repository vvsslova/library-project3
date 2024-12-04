package com.github.vvsslova.libraryrest.controllers;

import com.github.vvsslova.libraryrest.dto.BookDTO;
import com.github.vvsslova.libraryrest.dto.PersonDTO;
import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.services.BookService;
import com.github.vvsslova.libraryrest.services.LibraryService;
import com.github.vvsslova.libraryrest.util.bookErrors.BookNotFoundException;
import com.github.vvsslova.libraryrest.util.bookErrors.BookNotSavedException;
import com.github.vvsslova.libraryrest.util.hashing.IDHashing;
import com.github.vvsslova.libraryrest.util.mapper.BookMapper;
import com.github.vvsslova.libraryrest.util.mapper.PersonMapper;
import com.github.vvsslova.libraryrest.util.personErrors.PersonNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final LibraryService libraryService;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;

    @Autowired
    public BookController(BookService bookService, LibraryService libraryService, PersonMapper personMapper, BookMapper bookMapper) {
        this.bookService = bookService;
        this.libraryService = libraryService;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
    }

    @GetMapping()
    public List<BookDTO> getAllBooks(@RequestParam(value = "Sort.by", required = false) String sort,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage) {
        if (sort == null & page != null & itemsPerPage != null) {
            return bookService.findAll(PageRequest.of(page, itemsPerPage)).stream().map(bookMapper::convertToBookDTO).toList();
        } else if (sort != null & page == null & itemsPerPage == null) {
            return bookService.findAll(Sort.by(sort)).stream().map(bookMapper::convertToBookDTO).toList();
        } else if (sort != null & page != null & itemsPerPage != null) {
            return bookService.findAll(PageRequest.of(page, itemsPerPage, Sort.by(sort))).getContent().stream().map(bookMapper::convertToBookDTO).toList();
        }
        return bookService.findAll().stream().map(bookMapper::convertToBookDTO).toList();
    }

    @GetMapping("/{id}")
    public BookDTO showBook(@PathVariable("id") int id) {
        return bookMapper.convertToBookDTO(bookService.findOne(IDHashing.toOriginalId(id)));
    }

    @GetMapping("/{id}/person")
    public PersonDTO showPerson(@PathVariable("id") int id) {
        if (libraryService.getLentPerson(IDHashing.toOriginalId(id)) == null) {
            throw new PersonNotFoundException();
        }
        return personMapper.convertToPersonDTO(libraryService.getLentPerson(id));
    }

    @GetMapping("/{id}/delayResult")
    public boolean getDelayResult(@PathVariable("id") int id) {
        long today = new Date().getTime();
        Book book = bookService.findOne(IDHashing.toOriginalId(id));
        if (book.getLendDate() != null) {
            book.setDelayCheckResult(today - book.getLendDate().getTime() >= 864000000);
        }
        return book.isDelayCheckResult();
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid BookDTO book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            getBindingResult(bindingResult);
        }
        bookService.save(bookMapper.convertToBook(book));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid BookDTO book,
                                             BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            getBindingResult(bindingResult);
        }
        bookService.update(IDHashing.toOriginalId(id), bookMapper.convertToBook(book));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        try {
            bookService.findOne(IDHashing.toOriginalId(id));
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        }
        bookService.delete(IDHashing.toOriginalId(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}/returnBook")
    public ResponseEntity<HttpStatus> returnBook(@PathVariable("id") int id) {
        libraryService.returnBook(IDHashing.toOriginalId(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{bookID}/lend/{personID}")
    public ResponseEntity<HttpStatus> lendBook(@PathVariable("bookID") int bookID, @PathVariable("personID") int personID) {
        libraryService.lendBook(IDHashing.toOriginalId(bookID), IDHashing.toOriginalId(personID));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("/searchBook")
    public List<BookDTO> searchBook(@RequestParam(value = "title", required = false) String title) {
        return bookService.searchBook(title).stream().map(bookMapper::convertToBookDTO).toList();
    }

    private void getBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            msg.append(error.getField()).append("-").append(error.getDefaultMessage()).append(";");
        }
        throw new BookNotSavedException(msg.toString());
    }
}
