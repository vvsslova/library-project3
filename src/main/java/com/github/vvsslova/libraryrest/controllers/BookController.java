package com.github.vvsslova.libraryrest.controllers;

import com.github.vvsslova.libraryrest.dto.BookDTO;
import com.github.vvsslova.libraryrest.services.BookService;
import com.github.vvsslova.libraryrest.services.LibraryService;
import com.github.vvsslova.libraryrest.services.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<BookDTO> getAllBooks(@RequestParam(value = "Sort.by", required = false) String sort,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage) {
        if (sort == null & page != null & itemsPerPage != null) {
            return bookService.findAll(PageRequest.of(page, itemsPerPage)).getContent();
        } else if (sort != null & page == null & itemsPerPage == null) {
            return bookService.findAll(Sort.by(sort));
        } else if (sort != null & page != null & itemsPerPage != null) {
            return bookService.findAll(PageRequest.of(page, itemsPerPage, Sort.by(sort))).getContent();
        }
        return bookService.findAll();
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
            messageService.getBindingResult(bindingResult);
        }
        bookService.save(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@RequestBody @Valid BookDTO book,
                                          BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            messageService.getBindingResult(bindingResult);
        }
        bookService.update(id, book);
        return new ResponseEntity<>(book, HttpStatus.OK);
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

    @PutMapping("/lend")
    public ResponseEntity<HttpStatus> lendBook(@RequestParam(value = "person_id", required = false) Integer person_id,
                                               @RequestParam(value = "book_id", required = false) Integer book_id) {
        libraryService.lendBook(book_id, person_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/searchBook")
    public List<BookDTO> searchBook(@RequestParam(value = "title", required = false) String title) {
        return bookService.searchBook(title);
    }

    @GetMapping("/books")
    public List<BookDTO> showPersonsBooks(@RequestParam(value = "person_id", required = false) int person_id) {
        return libraryService.lentBooks(person_id);
    }
}
