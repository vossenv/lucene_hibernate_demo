package com.dm.teamquery.search;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

@EqualsAndHashCode
@NoArgsConstructor
@Getter @Setter
public class Search {


    private Set<String> fieldNames;
    private String OR_OPERATOR = "OR";
    private String AND_OPERATOR = "AND";
    private String AND_HOLDER = "%%";

    private String SPACE_HOLDER = "__";
    private String TAB_HOLDER = "@@@";
    private String TERM_SEPARATOR = "###";
    private final String andSearchPattern = "(?<=\\S)\\s+" + AND_OPERATOR + "\\s+(?=\\S)";
    private final String spaceTerms = "(\".*?\"|\\S*\\s*=\\s*\".*\"|\\S*\\s*=\\s*.*?(?=\\s))";

    private String query;
    private Pageable page;
    private Class entityType;
    private QueryGenerator queryGenerator;
    private SearchEntity searchEntity;
    private Set<String> searchTerms = new HashSet<>();

    public Search(Class entityType){this(entityType,"");}
    public Search(Class entityType, String query) {
        this(entityType, query, PageRequest.of(0, 100));
    }
    public Search(Class entityType, String query, Pageable page) {
        this.query = query;
        this.page = page;
        this.entityType = entityType;
        this.searchEntity = new SearchEntity(query);
        this.queryGenerator = new QueryGenerator(entityType);
        this.fieldNames = stream(entityType.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
        indexTerms();
    }

    private void indexTerms() {


        searchTerms = decode(encode(query));
        searchTerms = refine(searchTerms);

        System.out.println();
    }

    private Set<String> refine (Set<String> initial) {
        return initial.stream()
                .map(t -> t = stream(t.split("="))
                        .map(String::trim)
                        .collect(Collectors.joining("=")))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toSet());
    }

    private String encode(String qstring) {
        for (String t : match(spaceTerms, qstring)) {
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
                .collect(Collectors.toSet());
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

    public Set<String> setQuery(String query) {
        this.query = query;
        indexTerms();
        return this.searchTerms;
    }

}
