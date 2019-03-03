package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends TeamQueryException {
    public EntityNotFoundException(Object... errors) {
        super(errors);
    }
    public EntityNotFoundException(StackTraceElement [] trace, Object... errors) {
        super(trace, errors);
    }
}