package com.dm.teamquery.search;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class InvalidQueryException extends Exception {
    public InvalidQueryException(String message) {
        super(message);
        errorList.add(message);
    }

    private List<String> errorList = new ArrayList<>();

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}