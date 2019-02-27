package com.dm.teamquery.execption;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamQueryException extends Exception {

    private List<String> errorList = new ArrayList<>();

    public TeamQueryException(String message) {
        super(message);
        errorList.add(message);
    }

    public TeamQueryException(List<String> errors) {
        super();
        errorList = errors;
    }
}
