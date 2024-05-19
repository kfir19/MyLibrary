package com.kfir.mylibrary.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String errorMessage) {
        super(errorMessage);
    }
}
