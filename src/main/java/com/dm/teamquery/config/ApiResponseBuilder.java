package com.dm.teamquery.config;

import com.dm.teamquery.controller.SimplePage;
import com.dm.teamquery.model.SearchResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

//@SuppressWarnings("Duplicates")
public class ApiResponseBuilder {

    public static ResponseEntity<Object> buildApiResponse(Object body, SimplePage p, long startTime){

        HttpHeaders headers = new HttpHeaders();
        SearchResult s = (SearchResult) body;
        List<?> resultsList = s.getResultsList();
        int pageCount = (int) Math.ceil((double) s.getRowCount() / (double) p.getSize());

        headers.add("Page-Size", p.getSize().toString());
        headers.add("Current-Page", p.getPage().toString());
        headers.add("Previous-Page", String.valueOf( Integer.max(p.getPage() - 1, 0)));
        headers.add("Next-Page", String.valueOf((p.getPage() + 1)));
        headers.add("Result-Count", String.valueOf(s.getRowCount()));
        headers.add("Search-Time-Seconds", String.valueOf(s.getSearchTime()));
        headers.add("Total-Time-Seconds", String.valueOf((System.nanoTime() - startTime)*1.0e-9));
        headers.add("Original-Query", s.getOriginalQuery());
        headers.add("Page-Count", String.valueOf(pageCount));

        return new ResponseEntity<>(resultsList,headers, HttpStatus.OK);
    }
}
