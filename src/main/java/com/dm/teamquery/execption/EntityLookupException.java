package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityLookupException extends TeamQueryException {
    public EntityLookupException(String message) {  super(message); }
    public EntityLookupException(StackTraceElement [] trace, String... errors) {
        super(trace, errors);
    }
}