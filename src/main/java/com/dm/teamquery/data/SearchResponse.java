package com.dm.teamquery.data;

import io.vavr.API;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class SearchResponse {
    private SearchRequest request;
    private long rowCount;
    private Double searchTime;
    private List<?> resultsList;


    public SearchResponse(SearchRequest request){
        this.request = request;
    }

    public ResponseEntity getResponse (Class type, Class dest){

        Resources responseBody = new Resources(
                resultsList.stream()
                        .map(API.unchecked(o ->
                                dest.getConstructor(type)
                                        .newInstance(type.cast(o))))
                                            .collect(Collectors.toList()));

        responseBody.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "this"));
        return prepareResponse(responseBody);
    }

    private ResponseEntity prepareResponse (Object body) {

        HttpHeaders headers = new HttpHeaders();
        int pageCount = (int) Math.ceil((double) this.getRowCount() / (double) request.getSize());

        headers.add("Page-Size", request.getSize().toString());
        headers.add("Current-Page", String.valueOf(1 + request.getPage()));
        headers.add("Previous-Page", String.valueOf( Integer.max(request.getPage(), 0) + 1));
        headers.add("Next-Page", String.valueOf((request.getPage() + 2)));
        headers.add("Result-Count", String.valueOf(this.rowCount));
        headers.add("Search-Time-Seconds", String.valueOf(this.searchTime));
        headers.add("Total-Time-Seconds", String.valueOf((System.nanoTime() - request.getRequestTime())*1.0e-9));
        headers.add("Original-Query", request.getQuery());
        headers.add("Page-Count", String.valueOf(pageCount));
        return new ResponseEntity<>(body,headers, HttpStatus.OK);
    }
}


