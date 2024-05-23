package com.kfir.mylibrary.services;

import com.kfir.mylibrary.dto.BookDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BooksService {

    ResponseEntity<List<BookDTO>> findAllByTitle(String title);

    ResponseEntity<List<BookDTO>> findAllByAuthor(String author);

    ResponseEntity<List<BookDTO>> findAllByGenre(String genre);

    ResponseEntity<List<BookDTO>> findAll();

    ResponseEntity<List<BookDTO>> findAllByIsAvailable(boolean isAvailable);

    ResponseEntity<BookDTO> createNew(BookDTO bookDTO);

    ResponseEntity<BookDTO> updateBook(BookDTO bookDTO);

    ResponseEntity<BookDTO> borrowBook(String id);

    ResponseEntity<BookDTO> returnBook(String id);

    ResponseEntity<List<BookDTO>> findAllByDueDateIsBefore();
}
