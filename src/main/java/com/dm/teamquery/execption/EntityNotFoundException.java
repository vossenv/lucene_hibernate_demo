package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends TeamQueryException {
    public EntityNotFoundException(String... errors) { super(errors); }
    public EntityNotFoundException(StackTraceElement [] trace, String... errors) {
        super(trace, errors);
    }
}