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
import org.springframework.http.HttpStatus;
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

    @Override
    public ResponseEntity<BookDTO> findById(UUID id) {

        try {
            Optional<Book> book = repo.findById(id);
            if (book.isPresent()) {
                BookDTO bookDTO = ObjectMapperUtil.map(book.get(), BookDTO.class);
                return ResponseEntity.ok(bookDTO);
            } else {
                throw new BookNotFoundException("book not found");
            }
        } catch (BookNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> findAllByTitle(String title) {

        try {
            List<Book> books = repo.findAllByTitle(title);
            if (books != null && !books.isEmpty()) {

                List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);

                return ResponseEntity.ok(booksDTOs);
            } else {
                throw new EmptyResultsException(String.format("Search yielded no results, no results returned for the title: '%s'", title));
            }
        } catch (EmptyResultsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> findAllByAuthor(String author) {
        try {
            List<Book> books = repo.findAllByAuthor(author);
            if (books != null && !books.isEmpty()) {
                List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);

                return ResponseEntity.ok(booksDTOs);
            } else {
                throw new EmptyResultsException(String.format("Search yielded no results, no results returned for the author: '%s'", author));
            }
        } catch (EmptyResultsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> findAllByGenre(String genre) {
        try {
            List<Book> books = repo.findAllByGenre(genre);
            if (books != null && !books.isEmpty()) {
                List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);

                return ResponseEntity.ok(booksDTOs);
            } else {
                throw new EmptyResultsException(String.format("Search yielded no results, no results returned for the genre: '%s'", genre));
            }
        } catch (EmptyResultsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> findAll() {
        try {
            List<Book> books = repo.findAll(Sort.by(Sort.Direction.ASC, "title"));
            if (books != null && !books.isEmpty()) {
                List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);
                return ResponseEntity.ok(booksDTOs);
            } else {
                throw new EmptyResultsException("Search yielded no results");
            }
        }catch (EmptyResultsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> findAllByIsAvailable(boolean isAvailable) {
        try {
            List<Book> books = repo.findAllByIsAvailable(isAvailable);
            if (books != null && !books.isEmpty()) {
                List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);

                return ResponseEntity.ok(booksDTOs);
            } else {
                throw new EmptyResultsException("Search yielded no results, no available books found!");
            }
        } catch (EmptyResultsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<BookDTO> createNew(BookDTO bookDTO) {

        try {
            if (validationUtil.isBookValidForSave(bookDTO)) {
                Book book = repo.save(Book.builder()
                        .id(UUID.randomUUID())
                        .title(bookDTO.getTitle())
                        .author(bookDTO.getAuthor())
                        .genre(bookDTO.getGenre())
                        .isAvailable(true)
                        .build());

                return ResponseEntity.ok(ObjectMapperUtil.map(book, BookDTO.class));
            }
            else{
                throw new InvalidDataException("Invalid data, unable to create new book");
            }
        } catch (InvalidDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<BookDTO> updateBook(BookDTO bookDTO) {
        try {
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
            }
            else{
                throw new InvalidDataException("Invalid data, unable to update book");
            }
        } catch (BookNotFoundException | InvalidDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<BookDTO> borrowBook(String id) {
        try {
            Optional<Book> bookToBorrow = repo.findById(UUID.fromString(id));
            if (bookToBorrow.isPresent()) {
                if (bookToBorrow.get().isAvailable()) {
                    bookToBorrow.get().setBorrowedDate(LocalDate.now());
                    bookToBorrow.get().setDueDate(Utilities.getDueDate(14));
                    bookToBorrow.get().setAvailable(false);
                } else {
                    throw new WrongStatusException("You are trying to borrow an unavailable book");
                }
            } else {
                throw new BookNotFoundException("Unable to land the book requested due to: book not found!");
            }
            return ResponseEntity.ok(ObjectMapperUtil.map(repo.save(bookToBorrow.get()), BookDTO.class));
        } catch (WrongStatusException | BookNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<BookDTO> returnBook(String id) {
        try {
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
        } catch (WrongStatusException | BookNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> findAllByDueDateIsBefore(LocalDate today) {
        try {
            List<Book> books = repo.findAllByDueDateIsBefore(today);
            if (books != null && !books.isEmpty()) {
                List<BookDTO> booksDTOs = ObjectMapperUtil.mapAll(books, BookDTO.class);
                return ResponseEntity.ok(booksDTOs);
            }else {
                throw new EmptyResultsException("Search yielded no results, no overdue books found!");
            }
        } catch (EmptyResultsException e) {
           throw new RuntimeException(e);
        }
    }
}
