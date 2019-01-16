package com.dm.teamquery.search;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Search {

    public Set<String> fieldNames;
    public final static String OR_OPERATOR = "\\sOR\\s";
    public final static String AND_OPERATOR = "\\sAND\\s";
    private final static String quotedSearchPattern = "\".*?\"";
    private final static String keyTermQuoted = "(\\S*\\s*=\\s?\").*?\"";
    private final static String keyTermUnQuoted = "\\S*\\s*=\\s*.*?(?=\\s)";
    private final static String keyTerm = "(\\S*\\s*=\\s*\".*\"|\\S*\\s*=\\s*.*?(?=\\s))";
    private final static String spaceTerms = "(\".*?\"|\\S*\\s*=\\s*\".*\"|\\S*\\s*=\\s*.*?(?=\\s))";

    private Pageable page;
    private Class entityType;
    private String currentQuery;
    private String query;
    private Set<String> andTerms = new HashSet<>();
    private Set<String> orTerms = new HashSet<>();
    private Set<String> toProcess = new HashSet<>();
    private SearchEntity searchEntity;

    // TEMP
    private final static String queryKey = "query";
    private final static String processKey = "toProcess";
    private final static String andKey = "and";
    public final static String termKey = "terms";

    public Search(Class entityType, String query, Pageable page) {
        this.page = page;
        initializeType(entityType);
        initializeQuery(query);
    }

    public Search(Class type, String query) {
        this(type, query, PageRequest.of(0, 100));
    }

    private void initializeQuery(String query){
        this.searchEntity = new SearchEntity(query);
        this.query = query;
        this.currentQuery = query;
        indexTerms();
    }

    private void initializeType(Class entityType){
        this.entityType = entityType;
        this.fieldNames = Arrays.stream(entityType.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
    }

    public void setQuery(String query) {
        initializeQuery(query);
    }

    public void setEntityType(Class entityType) {
        initializeType(entityType);
    }

    // the main method
    private void indexTerms(){
        this.currentQuery = this.query;
        booleanFilter();

    }

    private void booleanFilter(){

        // replace significant spaces with __ to allow space splitting
        match(spaceTerms, query).forEach(t -> query = query.replace(t, t.replaceAll("\\s","__").replaceAll("\"","")));


        System.out.println();

    }

    private void booleanFilter1(Map<String, List<String>> searchPatterns) {

        asList(searchPatterns.get(queryKey).get(0).split(OR_OPERATOR)).forEach(p -> {
            String[] adds = (p.trim()).split(AND_OPERATOR);
            if (adds.length > 1)
                searchPatterns.get(andKey).add(stream(adds).map(String::trim).collect(Collectors.joining(AND_OPERATOR)));
            else
                searchPatterns.get(processKey).add(p.trim());
        });

        List<String> newAndList = new LinkedList<>();

        searchPatterns.get(andKey).forEach(val -> {
            if (val.matches(".*\\s+.*")){
                List<String> keyQuoted = match(keyTermQuoted, val);
                List<String> quoted = match(quotedSearchPattern, val);
                for (String q : keyQuoted) val = swapSpace(val, q);
                for (String q : quoted) val = swapSpace(val, q);
                newAndList.addAll(restoreSpace(val));
            } else {
                newAndList.add(val);
            }
        });

        searchPatterns.put(andKey, newAndList.stream().filter(v -> !v.isEmpty()).collect(Collectors.toList()));
    }

    private void standardFilter(Map<String, List<String>> searchPatterns) {

        if (searchPatterns.get(processKey).isEmpty()) return;

        List<String> keyQuoted = match(keyTermQuoted, searchPatterns.get(processKey).get(0));

        keyQuoted.forEach(kq -> {
            searchPatterns.get(termKey).add(kq.replaceAll("\"", ""));
            searchPatterns.get(processKey).add(0, searchPatterns.get(processKey).get(0).replace(kq, ""));
        });

        match(quotedSearchPattern, searchPatterns.get(processKey).get(0)).forEach(m -> {
            searchPatterns.get(termKey).add(m.replaceAll("\"", ""));
            searchPatterns.get(processKey).add(0, searchPatterns.get(processKey).get(0).replace(m, ""));
        });

        searchPatterns.get(termKey).addAll(searchPatterns.get(processKey).stream()
                .map(s -> s.split("\\s"))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList()));
    }


    private String swapSpace (String string, String term){
        return string.replaceAll(term, term.replaceAll("\\s+","__").replace("\"",""));
    }

    private List<String> restoreSpace (String string){
        return Arrays.stream(string.split("\\s")).map(s -> s.replaceAll("__"," ")).collect(Collectors.toList());
    }

    private List<String> match(String pattern, String text) {
        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group().trim());
        return new LinkedList<>(termList);
    }
}
