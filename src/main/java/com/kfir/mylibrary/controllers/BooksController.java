package com.kfir.mylibrary.controllers;

import com.kfir.mylibrary.dto.BookDTO;
import com.kfir.mylibrary.exceptions.BookNotFoundException;
import com.kfir.mylibrary.exceptions.EmptyResultsException;
import com.kfir.mylibrary.exceptions.InvalidDataException;
import com.kfir.mylibrary.exceptions.WrongStatusException;
import com.kfir.mylibrary.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BooksService service;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return service.findAll();
    }

    @GetMapping(value = "/title/{title}")
    public ResponseEntity<List<BookDTO>> getAllBooksByTitle(@PathVariable("title") String title) {
        try {
            return service.findAllByTitle(title);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/author/{author}")
    public ResponseEntity<List<BookDTO>> getAllBooksByAuthor(@PathVariable("author") String author) {
        try {
            return service.findAllByAuthor(author);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/genre/{genre}")
    public ResponseEntity<List<BookDTO>> getAllBooksByGenre(@PathVariable("genre") String genre) {
        try {
            return service.findAllByGenre(genre);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/allAvailable")
    public ResponseEntity<List<BookDTO>> getAllAvailableBooks() {
        try {
            return service.findAllByIsAvailable(true);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        try {
            return service.createNew(bookDTO);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO bookDTO) {
        try {
            return service.updateBook(bookDTO);
        }  catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/borrow/{id}")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable("id") String id) {
        try {
            return service.borrowBook(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping(value = "/return/{id}")
    public ResponseEntity<BookDTO> returnBook(@PathVariable("id") String id) {
        try {
            return service.returnBook(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/dateIsDue")
    public ResponseEntity<List<BookDTO>> getAllDateIsDueBooks() {
        try {
            return service.findAllByDueDateIsBefore(LocalDate.now());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
