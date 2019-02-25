package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityUpdateException extends TeamQueryException {
    public EntityUpdateException(String message) {
        super(message);
    }
}