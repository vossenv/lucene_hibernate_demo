package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEntityException extends TeamQueryException {
    public InvalidEntityException(List<String> errors){
        super(errors);
    }
    public InvalidEntityException(String message) {
        super(message);
    }
}