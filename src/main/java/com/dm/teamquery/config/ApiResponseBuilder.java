package com.dm.teamquery.config;

import com.dm.teamquery.controller.SimplePage;
import com.dm.teamquery.model.SearchResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@SuppressWarnings("Duplicates")
public class ApiResponseBuilder {

    public static ResponseEntity<Object> buildApiResponse(Object body, SimplePage p){

        HttpHeaders headers = new HttpHeaders();
        SearchResult s = (SearchResult) body;

        headers.add("Search-Size", p.getSize().toString());
        headers.add("Search-Page", p.getPage().toString());
        headers.add("Previous-Page", String.valueOf( Integer.max(p.getPage() - 1, 0)));
        headers.add("Next-Page", String.valueOf((p.getPage() + 1)));
        headers.add("Next-Size", String.valueOf(p.getSize()));
        headers.add("Result-Count", String.valueOf(s.getRowCount()));
        headers.add("Search-Time-Seconds", String.valueOf(s.getSearchTime()));
        headers.add("Original-Query", s.getOriginalQuery());

        return new ResponseEntity<>(s.getResultsList(),headers, HttpStatus.OK);
    }
}
