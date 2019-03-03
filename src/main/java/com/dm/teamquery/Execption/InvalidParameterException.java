package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidParameterException extends TeamQueryException {
    public InvalidParameterException(Object... errors) {
        super(errors);
    }
    public InvalidParameterException(StackTraceElement [] trace, Object... errors) {
        super(trace, errors);
    }
}