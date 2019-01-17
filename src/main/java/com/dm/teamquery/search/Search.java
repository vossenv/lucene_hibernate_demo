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

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Search {

    public Set<String> fieldNames;
    public final static String OR_OPERATOR = "OR";
    public final static String AND_OPERATOR = "AND";
    public final static String SPACE_HOLDER = "__";
    public final static String TAB_HOLDER = "@@@";
    public final static String TERM_SEPARATOR = "###";
    private final static String andSearchPattern = "(?<=\\S)\\s*" + AND_OPERATOR + "\\s*(?=\\S)";
    private final static String spaceTerms = "(\".*?\"|\\S*\\s*=\\s*\".*\"|\\S*\\s*=\\s*.*?(?=\\s))";

    private String query;
    private Pageable page;
    private Class entityType;
    private SearchEntity searchEntity;
    private Set<String> orTerms = new HashSet<>();

    public Search(Class type, String query) {
        this(type, query, PageRequest.of(0, 100));
    }

    public Search(Class entityType, String query, Pageable page) {
        this.query = query;
        this.page = page;
        this.entityType = entityType;
        this.searchEntity = new SearchEntity(query);
        this.fieldNames = stream(entityType.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
        indexTerms();
    }


    private void indexTerms() {
        booleanFilter();
    }
    private void booleanFilter() {
        orTerms = decode(encode(query));
        orTerms = orTerms.stream()
                .map(t -> t = stream(t.split("="))
                        .map(String::trim)
                        .collect(Collectors.joining("=")))
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toSet());
        System.out.println();
    }

    private String encode(String qstring) {
        for (String t : match(spaceTerms, qstring)) {
            qstring = qstring.replace(t, t
                    .replaceAll("\\t", TAB_HOLDER)
                    .replaceAll("\\s", SPACE_HOLDER)
                    .replaceAll("\"", ""));
        }
        return qstring
                .replaceAll(andSearchPattern, AND_OPERATOR)
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

    private List<String> match(String pattern, String text) {
        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group().trim());
        return new LinkedList<>(termList);
    }

    public void setQuery(String query) {
        this.query = query;
        indexTerms();
    }


}
