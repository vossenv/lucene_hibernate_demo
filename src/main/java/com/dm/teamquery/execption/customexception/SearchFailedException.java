package com.dm.teamquery.execption.customexception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SearchFailedException extends TeamQueryException {
    public SearchFailedException(Object... errors) {
        super(errors);
    }
    public SearchFailedException(StackTraceElement [] trace, Object... errors) {
        super(trace, errors);
    }
}