package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEntityIdException extends TeamQueryException {
    public InvalidEntityIdException(Object... errors) {
        super(errors);
    }
    public InvalidEntityIdException(StackTraceElement [] trace, Object... errors) {
        super(trace, errors);
    }
}