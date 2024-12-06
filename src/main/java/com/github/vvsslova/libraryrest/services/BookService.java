package com.github.vvsslova.libraryrest.services;

import com.github.vvsslova.libraryrest.dto.BookDTO;
import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotDeletedException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotFoundException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotSavedException;
import com.github.vvsslova.libraryrest.exception.entity.EntityNotUpdatedException;
import com.github.vvsslova.libraryrest.mapper.book.BookMapper;
import com.github.vvsslova.libraryrest.repositories.BookRepository;
import com.github.vvsslova.libraryrest.util.hashing.IDHashing;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::convertToBookDTO).toList();
    }

    public List<BookDTO> findAll(Sort sortCriterion) {
        return bookRepository.findAll(sortCriterion).stream().map(bookMapper::convertToBookDTO).toList();
    }

    public Page<BookDTO> findAll(Pageable pageVariables) {
        Page<Book> bookPage = bookRepository.findAll(pageVariables);
        List<BookDTO> bookDTOList = bookPage
                .getContent()
                .stream()
                .map(bookMapper::convertToBookDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(bookDTOList, pageVariables, bookPage.getTotalElements());
    }

    public BookDTO findBook(int id) {
        Optional<Book> fontBook = bookRepository.findById(IDHashing.hashingId(id));
        return fontBook
                .map(bookMapper::convertToBookDTO)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    // TODO
    @Transactional
    public void save(BookDTO book) {
        try {
            bookRepository.save(bookMapper.convertToBook(book));
        } catch (Exception e) {
            throw new EntityNotSavedException("Book not saved");
        }
    }

    @Transactional
    public void update(int id, BookDTO book) {
        try {
            Optional<Book> oldBook = bookRepository.findById(IDHashing.hashingId(id));
            if (oldBook.isEmpty()) {
                throw new EntityNotFoundException("Book not found");
            }
            Book updatedBook = bookMapper.convertToBook(book);
            updatedBook.setId(IDHashing.hashingId(id));
            updatedBook.setLentPerson(oldBook.get().getLentPerson());
            updatedBook.setLendDate(oldBook.get().getLendDate());
            bookRepository.save(updatedBook);
        } catch (Exception e) {
            throw new EntityNotUpdatedException("Book not updated");
        }
    }

    @Transactional
    public void delete(int id) {
        try {
            bookRepository.deleteById(IDHashing.hashingId(id));
        } catch (Exception e) {
            throw new EntityNotDeletedException("Book not deleted");
        }
    }

    public List<BookDTO> searchBook(String title) {
        return bookRepository
                .findByTitleStartingWith(title)
                .stream()
                .map(bookMapper::convertToBookDTO)
                .toList();
    }

    public boolean getDelayResult(int bookId) {
        Optional<Book> fountBook = bookRepository.findById(IDHashing.hashingId(bookId));
        if (fountBook.isEmpty()) {
            throw new EntityNotFoundException("Book not found");
        }
        long today = new Date().getTime();
        Book book = fountBook.get();
        if (book.getLendDate() != null) {
            book.setDelayCheckResult(today - book.getLendDate().getTime() >= 864000000);
        }
        return book.isDelayCheckResult();
    }
}
