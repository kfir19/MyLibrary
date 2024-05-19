package com.kfir.mylibrary.services;

import com.kfir.mylibrary.dto.BookDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BooksService {

    ResponseEntity<BookDTO> findById(UUID id);

    ResponseEntity<List<BookDTO>> findAllByTitle(String title);

    ResponseEntity<List<BookDTO>> findAllByAuthor(String author);

    ResponseEntity<List<BookDTO>> findAllByGenre(String genre);

    ResponseEntity<List<BookDTO>> findAll();

    ResponseEntity<List<BookDTO>> findAllByIsAvailable(boolean isAvailable);

    ResponseEntity<BookDTO> createNew(BookDTO bookDTO);

    ResponseEntity<BookDTO> updateBook(BookDTO bookDTO);

    ResponseEntity<BookDTO> borrowBook(String id);

    ResponseEntity<BookDTO> returnBook(String id);

    ResponseEntity<List<BookDTO>> findAllByDueDateIsBefore(LocalDate today);
}
