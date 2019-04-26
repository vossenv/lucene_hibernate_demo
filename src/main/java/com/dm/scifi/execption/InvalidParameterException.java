package com.dm.scifi.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidParameterException extends ScifiException {
    public InvalidParameterException(Object... errors) {
        super(errors);
    }
    public InvalidParameterException(StackTraceElement [] trace, Object... errors) {
        super(trace, errors);
    }
}