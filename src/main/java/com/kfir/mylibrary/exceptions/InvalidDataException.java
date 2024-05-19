package com.kfir.mylibrary.exceptions;

public class InvalidDataException extends Exception{
    public InvalidDataException(String errorMessage){
        super(errorMessage);
    }
}
