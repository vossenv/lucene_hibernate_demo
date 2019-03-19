package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Getter
@Setter
public class SearchGroup {

    private Map<String, SearchTerm> termList = new HashMap<>();
    private String currentQuery;
    private String originalQuery;

    public SearchGroup(String currentQuery){
        this.currentQuery = currentQuery;
        this.originalQuery = currentQuery;
    }

    public void encodeTerm(SearchTerm.Types type, String s){
        SearchTerm st = new SearchTerm(type, s);
        termList.put(st.getId(), st);
        currentQuery = currentQuery.replaceFirst(Pattern.quote(s), st.getId());
    }

    public String getNormalizedQuery(){
        StringBuilder sb = new StringBuilder(currentQuery);
        termList.keySet().forEach(k -> sb.replace(sb.indexOf(k), sb.indexOf(k) + k.length(), " " + k + " "));
        return sb.toString().trim().replaceAll("\\s+", " ");
    }

    public String getDecodedQuery(){
        return termList.values().stream()
                .sorted(Comparator.comparing(SearchTerm::getIndex))
                .map(SearchTerm::getQuotedValue)
                .collect(Collectors.joining(" "));
    }

    public String getLabeledTerms(){
        return termList.values().stream()
                .sorted(Comparator.comparing(SearchTerm::getIndex))
                .map(t -> t.getIndex() + "{" + t.getQuotedValue() + "}")
                .collect(Collectors.joining(" "));
    }

    public void encodeRemainingTerms(){
        stream(getNormalizedQuery().split(" "))
                .filter(k -> !termList.containsKey(k) && !k.isEmpty())
                .forEach(t -> encodeTerm(SearchTerm.Types.TEXT,t));
        indexAllTerms();
    }

    private void indexAllTerms(){
        final AtomicInteger count = new AtomicInteger();
        stream(getNormalizedQuery().split(" ")).forEach(t -> termList.get(t).setIndex(count.getAndIncrement()));
    }

}
