package com.kfir.mylibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MyLibraryApplication {
    public static void main(String[] args) {

        SpringApplication.run(MyLibraryApplication.class, args);
    }
}