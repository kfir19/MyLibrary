package com.kfir.mylibrary.servicesImpl;

import com.kfir.mylibrary.entities.Book;
import com.kfir.mylibrary.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@Service
public class OverDueService {

    @Autowired
    private BooksRepository repo;

    @Bean
    public void StartInBackground() {
        TimerTask checkForOverdueBooksTask = new TimerTask() {
            public void run() {
                List<Book> dueBooks = repo.findAllByDueDateIsBefore(LocalDate.now());
                if (dueBooks != null && !dueBooks.isEmpty()) {
                    dueBooks.stream().forEach(b -> System.out.println(String.format("%s was due in %s", b.getTitle(), b.getDueDate())));
                }
            }
        };

        Timer timer = new Timer("Overdue_books_timer");

        long delay = 0L;
        long period = 1000L * 60L * 60L * 24L;
        timer.scheduleAtFixedRate(checkForOverdueBooksTask, delay, period);
    }
}
