package com.kfir.mylibrary.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookDTO {

    private UUID id;
    private String title;
    private String author;
    private String genre;
    private LocalDate dueDate;
    private LocalDate borrowedDate;
}
