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

    private Pageable page;
    private Class entityType;
    private String currentQuery;
    private String query;
    private Set<String> andTerms = new HashSet<>();
    private Set<String> orTerms = new HashSet<>();
    private Set<String> toProcess = new HashSet<>();
    private SearchEntity searchEntity;

    public Search(Class entityType, String query, Pageable page) {
        this.searchEntity = new SearchEntity(query);
        initializeType(entityType);
        initializeQuery(query);
        this.page = page;
    }

    public Search(Class type, String query) {
        this(type, query, PageRequest.of(0, 100));
    }

    private void initializeQuery(String query){
        this.query = query;
        this.currentQuery = query;
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

    private String swapSpace (String string, String term){
        return  string.replaceAll(term, term.replaceAll("\\s+","__").replace("\"",""));
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
