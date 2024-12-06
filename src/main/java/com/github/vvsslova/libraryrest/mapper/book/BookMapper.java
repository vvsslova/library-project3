package com.github.vvsslova.libraryrest.mapper.book;

import com.github.vvsslova.libraryrest.dto.BookDTO;
import com.github.vvsslova.libraryrest.entity.Book;
import com.github.vvsslova.libraryrest.util.hashing.IDHashing;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    private final ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BookDTO convertToBookDTO(Book book) {
        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
        if (bookDTO.getId() == 0) {
            return bookDTO;
        }
        bookDTO.setId(IDHashing.hashingId(book.getId()));
        return bookDTO;
    }

    public Book convertToBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        if (book.getId() == 0) {
            return book;
        }
        book.setId(IDHashing.hashingId(bookDTO.getId()));
        return book;
    }
}