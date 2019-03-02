package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidParameterException extends TeamQueryException {
    public InvalidParameterException(String... errors) { super(errors); }
    public InvalidParameterException(StackTraceElement [] trace, String... errors) {
        super(trace, errors);
    }
}