package com.kfir.mylibrary.exceptions;

public class EmptyResultsException extends Exception{
    public EmptyResultsException(String errorMessage){
        super(errorMessage);
    }
}
