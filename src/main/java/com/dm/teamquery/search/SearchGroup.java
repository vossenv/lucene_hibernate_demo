package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Getter
@Setter
public class SearchGroup {

    private Map<String, SearchTerm> mappedTerms = new HashMap<>();
    private String currentQuery;
    private String originalQuery;

    public SearchGroup(String currentQuery){
        this.currentQuery = currentQuery;
        this.originalQuery = currentQuery;
    }

    public void encodeTerm(TermTypes type, String s){
        SearchTerm st = new SearchTerm(type, s);
        mappedTerms.put(st.getId(), st);
        currentQuery = currentQuery.replaceFirst(Pattern.quote(s), st.getId());
    }

    public String getNormalizedQuery(){
        StringBuilder sb = new StringBuilder(currentQuery);
        mappedTerms.keySet().forEach(k -> sb.replace(sb.indexOf(k), sb.indexOf(k) + k.length(), " " + k + " "));
        return sb.toString().trim().replaceAll("\\s+", " ");
    }

    public String getDecodedQuery(){
        return mappedTerms.values().stream()
                .sorted(Comparator.comparing(SearchTerm::getIndex))
                .map(t -> StringUtils.wrap(t.getValue(), t.getType() == TermTypes.QUOTED ? "\"" : ""))
                .collect(Collectors.joining(" "));
    }

    public String getLabeledTerms(){
        return mappedTerms.values().stream()
                .sorted(Comparator.comparing(SearchTerm::getIndex))
                .map(t -> "(" + t.getIndex() + ")" + t.getType().toString() +  "-{" + t.getValue() + "}")
                .collect(Collectors.joining(" "));
    }

    public SearchGroup encodeRemainingTerms(){
        stream(getNormalizedQuery().split(" "))
                .filter(k -> !mappedTerms.containsKey(k) && !k.isEmpty())
                .forEach(t -> encodeTerm(TermTypes.TEXT,t));
        indexAllTerms();
        return this;
    }

    private void indexAllTerms(){
        final AtomicInteger count = new AtomicInteger();
        stream(getNormalizedQuery().split(" ")).forEach(t -> mappedTerms.get(t).setIndex(count.getAndIncrement()));
    }
}
