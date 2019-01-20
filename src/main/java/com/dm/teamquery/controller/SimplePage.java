package com.dm.teamquery.controller;


import com.dm.teamquery.Execption.InvalidParameterException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Getter @Setter
@EqualsAndHashCode
public class SimplePage {

    private Integer size;
    private Integer page;
    private String query;
    private String URL;
    private String host;
    private Pageable pageable;
    private List<String> errors = new ArrayList<>();

    public SimplePage(HttpServletRequest request) throws InvalidParameterException {

        Set<String> headers = new HashSet<>(Collections.list(request.getHeaderNames()));

        this.host = request.getHeader("host");
        this.URL = request.getRequestURL().toString();
        this.query = headers.contains("query") ? request.getHeader("query") : "";
        String page = headers.contains("page") ? request.getHeader("page") : "0";
        String size = headers.contains("size") ? request.getHeader("size") : "100";

        this.size = validateParameter("size", size, 1, 1000);
        this.page = validateParameter("page", page, 0, Integer.MAX_VALUE);
        this.pageable = PageRequest.of(this.page, this.size);

        if (errors.size() > 0) {
            InvalidParameterException e = new InvalidParameterException();
            e.getErrorList().addAll(errors);
            throw e;
        }
    }

    private int validateParameter (String type, String param, int min, int max){
        try {
            int p = Integer.parseInt(param);
            if (p < min || p > max){
                errors.add("Valid range exceeded for " + type + ". Must be between " + min + " and " + max);
                return 0;
            } else return p;
        }
        catch (NumberFormatException e){
           errors.add("Error parsing " + type + ": '" + param + "'.  Please enter a valid integer between " + min + " and " + max);
           return 0;
        }
    }


}
