package com.kfir.mylibrary.exceptions;

public class BookNotFoundException extends Exception{
    public BookNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
