package com.dm.teamquery.controller;


import com.dm.teamquery.execption.InvalidParameterException;
import com.dm.teamquery.data.SearchResult;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Getter @Setter
@EqualsAndHashCode
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
public class SimplePage {

    private Integer size;
    private Integer page;
    private String query;
    private String URL;
    private String host;
    private Boolean includeDisabled;
    private Pageable pageable;
    private Long requestTime = System.nanoTime();
    private List<String> errors = new ArrayList<>();

    public SimplePage(HttpServletRequest request, Optional<String> disabled) throws InvalidParameterException {

        Set<String> headers = new HashSet<>(Collections.list(request.getHeaderNames()));
        this.includeDisabled = disabled.isPresent() && Boolean.parseBoolean(disabled.get());

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

    public ResponseEntity prepareResponse (Object body, SearchResult meta) {

        HttpHeaders headers = new HttpHeaders();
        int pageCount = (int) Math.ceil((double) meta.getRowCount() / (double) this.size);

        headers.add("Page-Size", this.size.toString());
        headers.add("Current-Page", this.page.toString());
        headers.add("Previous-Page", String.valueOf( Integer.max(this.page - 1, 0)));
        headers.add("Next-Page", String.valueOf((this.page + 1)));
        headers.add("Result-Count", String.valueOf(meta.getRowCount()));
        headers.add("Search-Time-Seconds", String.valueOf(meta.getSearchTime()));
        headers.add("Total-Time-Seconds", String.valueOf((System.nanoTime() - this.requestTime)*1.0e-9));
        headers.add("Original-Query", this.query);
        headers.add("Page-Count", String.valueOf(pageCount));
        return new ResponseEntity<>(body,headers, HttpStatus.OK);
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
