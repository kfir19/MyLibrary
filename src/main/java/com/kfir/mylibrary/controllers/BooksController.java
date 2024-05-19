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

    /**
     * Rest call for retrieving all books regardless of their status (available, unavailable)
     *
     * @return Response entity containing the result - list of all books
     */
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return service.findAll();
    }

    /**
     * Rest call for retrieving all books according to the provided title
     *
     * @param title - the param for the search
     * @return Response entity containing the result - list of all books with title equal to param title
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @GetMapping(value = "/title/{title}")
    public ResponseEntity<List<BookDTO>> getAllBooksByTitle(@PathVariable("title") String title) {
        try {
            return service.findAllByTitle(title);
        } catch (EmptyResultsException e) {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, e.getMessage());
        }
    }

    /**
     * Rest call for retrieving all books according to the provided author
     *
     * @param author - the param for the search
     * @return Response entity containing the result - list of all books with author equal to param author
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @GetMapping(value = "/author/{author}")
    public ResponseEntity<List<BookDTO>> getAllBooksByAuthor(@PathVariable("author") String author) {
        try {
            return service.findAllByAuthor(author);
        } catch (EmptyResultsException e) {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, e.getMessage());
        }
    }

    /**
     * Rest call for retrieving all books according to the provided genre
     *
     * @param genre - the param for the search
     * @return Response entity containing the result - list of all books with genre equal to param genre
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @GetMapping(value = "/genre/{genre}")
    public ResponseEntity<List<BookDTO>> getAllBooksByGenre(@PathVariable("genre") String genre) {
        try {
            return service.findAllByGenre(genre);
        } catch (EmptyResultsException e) {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, e.getMessage());
        }
    }

    /**
     * Rest call for retrieving all available books
     *
     * @return Response entity containing the result - list of all available books
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @GetMapping(value = "/allAvailable")
    public ResponseEntity<List<BookDTO>> getAllAvailableBooks() {
        try {
            return service.findAllByIsAvailable(true);
        } catch (EmptyResultsException e) {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, e.getMessage());
        }
    }

    /**
     * Rest call for creating new book
     *
     * @param bookDTO - according to this param the new book will be created
     * @return Response entity containing the newly created book
     * @throws InvalidDataException is thrown in case the bookDTO param is invalid. new book will NOT be created
     */
    @PostMapping(value = "/create")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        try {
            return service.createNew(bookDTO);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Rest call for updating an existing book
     *
     * @param bookDTO - according to this param an existing book will be updated
     * @return Response entity containing the updated existing book
     * @throws BookNotFoundException is thrown in case the book to update was not found in the db
     * @throws InvalidDataException  is thrown in case the provided bookDTO is invalid. book will NOT be updated
     */
    @PutMapping(value = "/update")
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO bookDTO) {
        try {
            return service.updateBook(bookDTO);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Rest call for borrowing a book
     *
     * @param id - according to this param the book is borrowed
     * @return Response entity containing the borrowed updated book
     * @throws BookNotFoundException is thrown in case the book to borrow was not found in the db
     * @throws WrongStatusException  is thrown when trying to change the status of the book from available
     *                               to unavailable and the status is already unavailable
     */
    @GetMapping(value = "/borrow/{id}")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable("id") String id) {
        try {
            return service.borrowBook(id);
        } catch (WrongStatusException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Rest call for returning a book
     *
     * @param id - according to this param the book is returned
     * @return Response entity containing the returned updated book
     * @throws BookNotFoundException is thrown in case the book to return was not found in the db
     * @throws WrongStatusException  is thrown when trying to change the status of the book from unavailable
     *                               to available and the status is already available
     */
    @PutMapping(value = "/return/{id}")
    public ResponseEntity<BookDTO> returnBook(@PathVariable("id") String id) {
        try {
            return service.returnBook(id);
        } catch (WrongStatusException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Rest call for getting all overdue books
     *
     * @return Response entity containing a list of the overdue books
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @GetMapping(value = "/dateIsDue")
    public ResponseEntity<List<BookDTO>> getAllDateIsDueBooks() {
        try {
            return service.findAllByDueDateIsBefore(LocalDate.now());
        } catch (EmptyResultsException e) {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, e.getMessage());
        }
    }
}
