package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeleteFailedException extends TeamQueryException {
    public DeleteFailedException(String message) {
        super(message);
    }
}