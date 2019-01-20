package com.dm.teamquery.config;

import com.dm.teamquery.model.SimplePage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SuppressWarnings("Duplicates")
public class ApiResponseBuilder {

    public static ResponseEntity<Object> buildApiResponse(Object body, SimplePage p){

        HttpHeaders headers = new HttpHeaders();

        headers.add("Search-Size", p.getSize().toString());
        headers.add("Search-Page", p.getPage().toString());

        headers.add("Next-Page", String.valueOf((p.getPage() + 1)));
        headers.add("Next-Size", String.valueOf(p.getSize()));
        headers.add("Result-Count", (String.valueOf(((List<?>)body).size())));

        return new ResponseEntity<>(body,headers, HttpStatus.OK);
    }
}
