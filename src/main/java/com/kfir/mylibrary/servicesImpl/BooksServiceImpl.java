package com.kfir.mylibrary.servicesImpl;

import com.kfir.mylibrary.dto.BookDTO;
import com.kfir.mylibrary.entities.Book;
import com.kfir.mylibrary.exceptions.BookNotFoundException;
import com.kfir.mylibrary.exceptions.EmptyResultsException;
import com.kfir.mylibrary.exceptions.InvalidDataException;
import com.kfir.mylibrary.exceptions.WrongStatusException;
import com.kfir.mylibrary.repositories.BooksRepository;
import com.kfir.mylibrary.services.BooksService;
import com.kfir.mylibrary.utils.ObjectMapperUtil;
import com.kfir.mylibrary.utils.Utilities;
import com.kfir.mylibrary.utils.validationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BooksRepository repo;

    /**
     * Method for finding all books by their title
     *
     * @param title - param for finding the books
     * @return Response entity with a list of the found books and status code ok (200)
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @Override
    public ResponseEntity<List<BookDTO>> findAllByTitle(String title) {
        List<Book> books = repo.findAllByTitle(title);
        if (books != null && !books.isEmpty()) {
            List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);
            return ResponseEntity.ok(booksDTOs);
        } else {
            throw new EmptyResultsException(String.format("Search yielded no results, no results returned for the title: '%s'", title));
        }
    }

    /**
     * Method for finding all books by their author
     *
     * @param author - param for finding the books
     * @return Response entity with a list of the found books and status code ok (200)
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @Override
    public ResponseEntity<List<BookDTO>> findAllByAuthor(String author) {
        List<Book> books = repo.findAllByAuthor(author);
        if (books != null && !books.isEmpty()) {
            List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);
            return ResponseEntity.ok(booksDTOs);
        } else {
            throw new EmptyResultsException(String.format("Search yielded no results, no results returned for the author: '%s'", author));
        }
    }

    /**
     * Method for finding all books by their genre
     *
     * @param genre - param for finding the books
     * @return Response entity with a list of the found books and status code ok (200)
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @Override
    public ResponseEntity<List<BookDTO>> findAllByGenre(String genre) {
        List<Book> books = repo.findAllByGenre(genre);
        if (books != null && !books.isEmpty()) {
            List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);
            return ResponseEntity.ok(booksDTOs);
        } else {
            throw new EmptyResultsException(String.format("Search yielded no results, no results returned for the genre: '%s'", genre));
        }
    }

    /**
     * Method for finding all books regardless of their status
     *
     * @return Response entity with a list of the found books and status code ok (200)
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @Override
    public ResponseEntity<List<BookDTO>> findAll() {

        List<Book> books = repo.findAll(Sort.by(Sort.Direction.ASC, "title"));
        if (!books.isEmpty()) {
            List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);
            return ResponseEntity.ok(booksDTOs);
        } else {
            throw new EmptyResultsException("Search yielded no results");
        }
    }

    /**
     * Method for finding all books by their availability status
     *
     * @param isAvailable - param for finding the books
     * @return Response entity with a list of the found books and status code ok (200)
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @Override
    public ResponseEntity<List<BookDTO>> findAllByIsAvailable(boolean isAvailable) {

        List<Book> books = repo.findAllByIsAvailable(isAvailable);
        if (books != null && !books.isEmpty()) {
            List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);

            return ResponseEntity.ok(booksDTOs);
        } else {
            throw new EmptyResultsException("Search yielded no results, no available books found!");
        }
    }

    /**
     * Method for creating new book
     *
     * @param bookDTO - according to this param a new book is created
     * @return Response entity with the newly created book and status code ok (200)
     * @throws InvalidDataException is thrown in case the param (bookDTO) provided is invalid
     */
    @Override
    public ResponseEntity<BookDTO> createNew(BookDTO bookDTO) {
        if (validationUtil.isBookValidForSave(bookDTO)) {
            Book book = repo.save(Book.builder()
                    .id(UUID.randomUUID())
                    .title(bookDTO.getTitle())
                    .author(bookDTO.getAuthor())
                    .genre(bookDTO.getGenre())
                    .isAvailable(true)
                    .build());

            return ResponseEntity.ok(ObjectMapperUtil.map(book, BookDTO.class));
        } else {
            throw new InvalidDataException("Invalid data, unable to create new book");
        }
    }

    /**
     * Method for updating an existing book
     *
     * @param bookDTO - according to this param the existing book will be updated
     * @return Response entity with the updated book and status code ok (200)
     * @throws InvalidDataException  is thrown in case the param (bookDTO) provided is invalid
     * @throws BookNotFoundException is thrown in case the book to update does not exist
     */
    @Override
    public ResponseEntity<BookDTO> updateBook(BookDTO bookDTO) {

        if (validationUtil.isBookValidForSave(bookDTO)) {
            Optional<Book> bookToUpdate = repo.findById(bookDTO.getId());
            if (bookToUpdate.isPresent()) {
                bookToUpdate.get().setTitle(bookDTO.getTitle());
                bookToUpdate.get().setAuthor(bookDTO.getAuthor());
                bookToUpdate.get().setGenre(bookDTO.getGenre());
                bookToUpdate.get().setDueDate(bookDTO.getDueDate());
                bookToUpdate.get().setBorrowedDate(bookDTO.getBorrowedDate());
            } else {
                throw new BookNotFoundException(String.format("Unable to update the book '%s', book not found", bookDTO.getTitle()));
            }
            return ResponseEntity.ok(ObjectMapperUtil.map(repo.save(bookToUpdate.get()), BookDTO.class));
        } else {
            throw new InvalidDataException("Invalid data, unable to update book");
        }
    }

    /**
     * Method for borrowing a book
     *
     * @param id - according to this param the user can borrow the correct book
     * @return Response entity with an updated borrowed book and status code ok (200)
     * @throws WrongStatusException  is thrown in case trying to change the book status to the same existing status
     * @throws BookNotFoundException is thrown in case the book to borrow does not exist
     */
    @Override
    public ResponseEntity<BookDTO> borrowBook(String id) {
        Optional<Book> bookToBorrow = repo.findById(UUID.fromString(id));
        if (bookToBorrow.isPresent()) {
            if (bookToBorrow.get().isAvailable()) {
                bookToBorrow.get().setBorrowedDate(LocalDate.now());
                bookToBorrow.get().setDueDate(Utilities.getDueDate(14));
                bookToBorrow.get().setAvailable(false);
            } else {
                throw new WrongStatusException(String.format("You are trying to borrow an unavailable book. The book will be available again on %s", bookToBorrow.get().getDueDate()));
            }
        } else {
            throw new BookNotFoundException("Unable to land the book requested due to: book not found!");
        }
        return ResponseEntity.ok(ObjectMapperUtil.map(repo.save(bookToBorrow.get()), BookDTO.class));
    }

    /**
     * Method for returning a book
     *
     * @param id - according to this param the user can return a book
     * @return Response entity with an updated returned book and status code ok (200)
     * @throws WrongStatusException  is thrown in case trying to change the book status to the same existing status
     * @throws BookNotFoundException is thrown in case the book to return does not exist
     */
    @Override
    public ResponseEntity<BookDTO> returnBook(String id) {
        Optional<Book> bookToReturn = repo.findById(UUID.fromString(id));
        if (bookToReturn.isPresent()) {
            if (!bookToReturn.get().isAvailable()) {
                bookToReturn.get().setBorrowedDate(null);
                bookToReturn.get().setDueDate(null);
                bookToReturn.get().setAvailable(true);
            } else {
                throw new WrongStatusException("You are trying to return a book that is already returned");
            }
        } else {
            throw new BookNotFoundException("Unable to accept the returned book due to: book not found!");
        }
        return ResponseEntity.ok(ObjectMapperUtil.map(repo.save(bookToReturn.get()), BookDTO.class));
    }

    /**
     * Method for finding all overdue books
     *
     * @return Response entity with a list of all overdue books and status code ok (200)
     * @throws EmptyResultsException is thrown in case the search yielded no results
     */
    @Override
    public ResponseEntity<List<BookDTO>> findAllByDueDateIsBefore() {
        List<Book> books = repo.findAllByDueDateIsBefore(LocalDate.now());
        if (books != null && !books.isEmpty()) {
            List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);
            return ResponseEntity.ok(booksDTOs);
        } else {
            throw new EmptyResultsException("Search yielded no results, no overdue books found!");
        }
    }
}
