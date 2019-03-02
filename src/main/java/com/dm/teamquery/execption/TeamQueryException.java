package com.dm.teamquery.execption;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class TeamQueryException extends Exception {
    private List<String> errorList = new ArrayList<>();
    public TeamQueryException(String... args) {
        super();
        errorList.addAll(Arrays.asList(args));
    }
    public TeamQueryException(StackTraceElement[] trace, String... args) {
        super();
        this.setStackTrace(trace);
        errorList.addAll(Arrays.asList(args));
    }
}
