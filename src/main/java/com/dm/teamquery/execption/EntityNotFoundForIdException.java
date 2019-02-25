package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundForIdException extends TeamQueryException {
    public EntityNotFoundForIdException(String message) {
        super(message);
    }
}