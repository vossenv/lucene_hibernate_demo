package com.dm.scifi.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SearchFailedException extends ScifiException {
    public SearchFailedException(Object... errors) {
        super(errors);
    }
    public SearchFailedException(StackTraceElement [] trace, Object... errors) {
        super(trace, errors);
    }
}