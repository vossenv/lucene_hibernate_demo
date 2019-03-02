package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEntityIdException extends TeamQueryException {
    public InvalidEntityIdException(String... errors) { super(errors); }
    public InvalidEntityIdException(StackTraceElement [] trace, String... errors) {
        super(trace, errors);
    }
}