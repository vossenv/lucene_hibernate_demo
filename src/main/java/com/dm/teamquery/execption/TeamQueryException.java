package com.dm.teamquery.execption;


import java.util.ArrayList;
import java.util.List;


public class TeamQueryException extends Exception {

    public TeamQueryException(String message) {
        super(message);
        errorList.add(message);
    }

    public TeamQueryException(List<String> errors){
        super();
        errorList = errors;
    }

    private  List<String> errorList = new ArrayList<>();
    public List<String> getErrorList() {
        return errorList;
    }
    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}