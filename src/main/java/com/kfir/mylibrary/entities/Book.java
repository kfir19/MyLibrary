package com.kfir.mylibrary.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("books")
public class Book {

    @Id
    private UUID id;
    private String title;
    private String author;
    private String genre;
    private LocalDate dueDate;
    private LocalDate borrowedDate;
    private boolean isAvailable;

    public Book(String title, String author, String genre) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return String.format("%s by %s", title, author);
    }
}
