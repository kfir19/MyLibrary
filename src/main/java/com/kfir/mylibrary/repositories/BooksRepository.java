package com.kfir.mylibrary.repositories;

import com.kfir.mylibrary.entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BooksRepository extends MongoRepository<Book, UUID> {

    List<Book> findAllByTitle(String title);

    List<Book> findAllByAuthor(String author);

    List<Book> findAllByGenre(String genre);

    List<Book> findAllByIsAvailable(boolean isAvailable);

    List<Book> findAllByDueDateIsBefore(LocalDate today);

}
