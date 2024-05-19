package com.kfir.mylibrary.exceptions;

public class EmptyResultsException extends RuntimeException {
    public EmptyResultsException(String errorMessage) {
        super(errorMessage);
    }
}
