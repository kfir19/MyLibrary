package com.kfir.mylibrary.exceptions;

public class WrongStatusException extends Exception{
    public WrongStatusException(String errorMessage){
        super(errorMessage);
    }
}
