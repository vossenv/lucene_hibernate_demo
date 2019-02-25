package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadEntityException extends TeamQueryException {
    public BadEntityException (String message) {
        super(message);
    }
}