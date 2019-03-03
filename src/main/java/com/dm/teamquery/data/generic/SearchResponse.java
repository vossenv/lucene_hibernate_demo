package com.dm.teamquery.data.generic;

import com.dm.teamquery.execption.EntityNotFoundException;
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

import static java.lang.Math.min;


@Data
public class SearchResponse {
    private SearchRequest request;
    private long rowCount;
    private Double searchTime;
    private List<?> resultsList;


    public SearchResponse(SearchRequest request){
        this.request = request;
    }

    public ResponseEntity getResponse (Class type, Class dest) throws EntityNotFoundException{

        Resources responseBody = new Resources(
                resultsList.stream()
                        .map(API.unchecked(o ->
                                dest.getConstructor(type)
                                        .newInstance(type.cast(o))))
                                            .collect(Collectors.toList()));

        return prepareResponse(responseBody);
    }

    private ResponseEntity prepareResponse (Resources body) throws EntityNotFoundException{

        HttpHeaders headers = new HttpHeaders();
        int pageCount = (int) Math.ceil((double) this.getRowCount() / (double) request.getSize());
        int next = min((request.getPage() + 2),pageCount);
        int prev = Integer.max(request.getPage(), 1);

        if (request.getPage() + 1 > pageCount)
            throw new EntityNotFoundException(String.format("Requested page (%d) does not exist", request.getPage()));

        headers.add("Page-Size", request.getSize().toString());
        headers.add("Current-Page", String.valueOf(1 + request.getPage()));
        headers.add("Previous-Page", String.valueOf(prev));
        headers.add("Next-Page", String.valueOf(next));
        headers.add("Result-Count", String.valueOf(this.rowCount));
        headers.add("Search-Time-Seconds", String.valueOf(this.searchTime));
        headers.add("Total-Time-Seconds", String.valueOf((System.nanoTime() - request.getRequestTime())*1.0e-9));
        headers.add("Original-Query", request.getQuery());
        headers.add("Page-Count", String.valueOf(pageCount));

        String lastCall = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();

        body.add(new Link(lastCall, "self"));
        body.add(new Link(lastCall.replaceAll("(page=)\\d*","page="+String.valueOf(next)), "next"));
        body.add(new Link(lastCall.replaceAll("(page=)\\d*","page="+String.valueOf(prev)), "previous"));

        return new ResponseEntity<>(body,headers, HttpStatus.OK);
    }
}


