package com.kfir.mylibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Book is in the opposite status")
public class WrongStatusException extends RuntimeException {
    public WrongStatusException(String errorMessage) {
        super(errorMessage);
    }
}
