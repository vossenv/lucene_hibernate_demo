package com.dm.teamquery.search;


import com.dm.teamquery.search.SearchTerm.Types;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@EqualsAndHashCode
@NoArgsConstructor
public class Search2 {

    @Getter
    private final String AND_OPERATOR = "AND";
    @Getter
    private final String OR_OPERATOR = "OR";
    @Getter
    @Setter
    private Set<String> fieldNames;

    @Getter
    private final String OR_HOLDER = ";=@!&@";
    private final String SPACE_HOLDER = "!a#!%";
    private final String TAB_HOLDER = "@&%*";
    private final String TERM_SEPARATOR = "#`]//:";
    private final String WHITESPACE = "(?<=\\S)\\s+(?=\\S)";

    private final String SPACE_SEARCH = "(?<=\\S)[\\t\\f ]+?(?=\\S)";
    private final String QUOTE_SEARCH = "(?<=^|\\s)\".*?\"(?=$|\\s)";
    private final String GROUP_SEARCH = "(?<=\\().*?(?=\\))";
    private final String KEYWORD_SEARCH = "\\S*\\s*=\\s*\".*?\"|\\S*\\s*=\\s*\\S*";
    private final String PAREN_SEARCH = "(?<=\\(|\\s|^)\\(|\\)(?=\\)|\\s|$)";
    private final String badKeyTerms = "(\\S*\\s*=\\s*$|^\\s*=\\S*)";

    private final String AND_SEARCH = WHITESPACE + AND_OPERATOR + WHITESPACE; //+ "|" + SPACE_SEARCH;
    private final String OR_SEARCH = WHITESPACE + OR_OPERATOR + WHITESPACE;

    private Class entityType;
    @Getter
    private String query;
    @Getter
    private Set<String> searchTerms = new HashSet<>();
    @Getter
    @Setter
    Map<String, SearchTerm> mappedTerms = new HashMap<>();
    @Getter
    @Setter
    private QueryGenerator queryGenerator;

    public Search2(Class entityType, String query) {
        this.query = query;
        this.entityType = entityType;
        this.fieldNames = stream(entityType.getDeclaredFields()).map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new));
        stream(entityType.getSuperclass().getDeclaredFields()).map(Field::getName).forEach(fieldNames::add);
        this.queryGenerator = new QueryGenerator(entityType.getSimpleName(), this.fieldNames);
        this.queryGenerator.setOR_HOLDER(OR_HOLDER);
        indexTerms();
    }

    public Search2(Class entityType) {
        this(entityType, "");
    }

    private void indexTerms() {

        query = "this AND \"a orb\" author=hello th(at (plus OR this)";
        query = encode(query);
        System.out.println();
    }

    private void filterSpecialTerms(String regex, SearchTerm.Types type, SearchGroup g) {
        match(regex, g.getCurrentQuery()).forEach(t -> g.encodeTerm(type, t));
    }

    private String encode(String qstring) {

        SearchGroup group = new SearchGroup(qstring);
        filterSpecialTerms(QUOTE_SEARCH, Types.QUOTED, group);
        filterSpecialTerms(KEYWORD_SEARCH, Types.KEYWORD, group);
        filterSpecialTerms(OR_SEARCH, Types.OR, group);
        filterSpecialTerms(AND_SEARCH, Types.AND, group);
        filterSpecialTerms(WHITESPACE, Types.AND, group);

        group.encodeRemainingTerms();

        String q = group.getCurrentQuery();
        String t = group.getNormalizedQuery();
        String gh = group.getDecodedQuery();
        String gg = group.getLabeledTerms();

        return qstring;

    }

    private List<String> match(String pattern, String text) {
        List<String> termList = new LinkedList<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group());
        return new LinkedList<>(termList);
    }


    public Search2 setQuery(String query) {
        this.query = query;
        indexTerms();
        return this;
    }
}

//    private boolean isKeyword(String s) {
//        return !fieldNames.contains(s.split("=")[0].trim());
//    }

