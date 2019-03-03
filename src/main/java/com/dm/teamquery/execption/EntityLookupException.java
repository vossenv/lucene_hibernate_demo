package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityLookupException extends TeamQueryException {
    public EntityLookupException(Object... errors) {
        super(errors);
    }
    public EntityLookupException(StackTraceElement [] trace, Object... errors) {
        super(trace, errors);
    }
}