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

import static java.util.Arrays.stream;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

@EqualsAndHashCode
@NoArgsConstructor
public class Search {

    @Getter private String AND_OPERATOR = "AND";
    @Getter @Setter private String OR_OPERATOR = "OR";
    @Getter @Setter private Set<String> fieldNames;

    private final String AND_HOLDER = ";=@!&@";
    private final String SPACE_HOLDER = "!a#!%";
    private final String TAB_HOLDER = "@&%*";
    private final String TERM_SEPARATOR = "#`]//:";

    private final String badKeyTerms = "(\\S*\\s*=\\s*$|^\\s*=\\S*)";
    private final String specialTerms = "(\\S*\\s*=\\s*\".*?\"|\\S*\\s*=\\s*\\S*|\".*?\"|\\S*\\s*=\\s*\".*\"|\\S*\\s*=\\s*.*?(?=\\s))";
    private String andSearchPattern = "(?<=\\S)\\s+" + AND_OPERATOR + "\\s+(?=\\S)";

    @Getter private String query;
    @Getter private Class entityType;
    @Getter private SearchEntity searchEntity;
    @Getter private Set<String> searchTerms = new HashSet<>();
    @Getter @Setter private Pageable page;
    @Getter @Setter private QueryGenerator queryGenerator;


    public Search(Class entityType, String query, Pageable page) {
        this.query = query;
        this.page = page;
        this.entityType = entityType;
        this.searchEntity = new SearchEntity(query);
        this.queryGenerator = new QueryGenerator(entityType);
        this.fieldNames = stream(entityType.getDeclaredFields())
                .map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new));
        this.queryGenerator.setAND_HOLDER(AND_HOLDER);
        indexTerms();
    }

    public Search(Class entityType){this(entityType,"");}
    public Search(Class entityType, String query) {
        this(entityType, query, PageRequest.of(0, 100));
    }

    private void indexTerms() {
        searchTerms = decode(encode(query));
        searchTerms = refine(searchTerms);
    }

    private Set<String> refine (Set<String> initial) {
        return initial.stream()
                .map(t -> t = match(badKeyTerms, t).size() == 0 ?
                        stream(t.split("="))
                                .map(String::trim).collect(Collectors.joining("=")) : t)
                .map(String::trim)
                .filter(t -> !t.isEmpty() )
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String encode(String qstring) {
        for (String t : match(specialTerms, qstring)) {
            qstring = qstring.replace(t, t
                    .replaceAll("\\t", TAB_HOLDER)
                    .replaceAll("\\s", SPACE_HOLDER)
                    .replaceAll("\"", ""));
        }
        return qstring
                .replaceAll(andSearchPattern, AND_HOLDER)
                .replaceAll(OR_OPERATOR, " ")
                .replaceAll("\\s+", TERM_SEPARATOR);
    }

    private Set<String> decode(String toDecode) {
        return asSet(toDecode.split(TERM_SEPARATOR))
                .stream()
                .map(t -> t = t
                        .replaceAll(TAB_HOLDER, "\\t")
                        .replaceAll(SPACE_HOLDER, " "))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String getDatabaseQuery(){
        return this.queryGenerator.generateQuery(searchTerms);
    }

    private List<String> match(String pattern, String text) {
        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group().trim());
        return new LinkedList<>(termList);
    }

    public Search setQuery(String query) {
        this.query = query;
        indexTerms();
        return this;
    }

    public void setAND_OPERATOR(String newAnd) {
        this.andSearchPattern = this.andSearchPattern.replace(AND_OPERATOR,newAnd);
        this.AND_OPERATOR = newAnd;
    }

}
