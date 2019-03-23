package com.dm.teamquery.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class SearchBuilder {

    private static final String WHITESPACE = "(?<=\\S)\\s+(?=\\S)";
    private static final String QUOTE_SEARCH = "(?<=^|\\s)((?<!\\\\)\").*?((?<!\\\\)\")(?=$|\\s)";
    private static final String KEYWORD_SEARCH = "\\S*\\s*=\\s*\".*?\"|\\S*\\s*=\\s*\\S*";
    private static final String AND_SEARCH = WHITESPACE + TermTypes.AND.name + WHITESPACE;
    private static final String OR_SEARCH = WHITESPACE + TermTypes.OR.name + WHITESPACE;

    @Getter
    private String query = "";

    @Getter
    private SearchGroup searchGroup;

    @Getter @Setter
    private Class entityType = Object.class;

    @Getter @Setter
    private QueryGenerator queryGenerator;

    public SearchBuilder(Class entityType, String query) {
        init(entityType, query);
    }

    public SearchBuilder(String query) {
        init(Object.class, query);
    }

    public SearchBuilder(Class entityType) {
        init(entityType, "");
    }

    public SearchBuilder setQuery(String query) {
        init(this.entityType, query);
        return this;
    }

    private void init(Class entityType, String query){
        Assert.notNull(entityType,"Invalid entity type: null");
        Assert.notNull(query,"Invalid query: null");
        this.query = query;
        this.entityType = entityType;
        this.searchGroup = parseQuery(query);
    }

    private SearchGroup parseQuery(String queryString) {
        SearchGroup group = new SearchGroup(queryString);
        encodeTerms(QUOTE_SEARCH, TermTypes.QUOTED, group);
        encodeTerms(KEYWORD_SEARCH, TermTypes.KEYWORD, group);
        encodeTerms(OR_SEARCH, TermTypes.OR, group);
        encodeTerms(AND_SEARCH, TermTypes.AND, group);
        encodeTerms(WHITESPACE, TermTypes.AND, group);

//        String x = group.getCurrentQuery();
//        String y = group.splitNonIndexedQuery();
        group.encodeRemainingTermsAndIndex();
        return group;
    }

    private List<String> match(String pattern, String text) {
        List<String> termList = new LinkedList<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group());
        return new LinkedList<>(termList);
    }

    private void encodeTerms(String regex, TermTypes type, SearchGroup g) {
        match(regex, g.getCurrentQuery()).forEach(t -> g.encodeTerm(type, t));
    }
}

//        this.fieldNames = stream(entityType.getDeclaredFields()).map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new));
//                stream(entityType.getSuperclass().getDeclaredFields()).map(Field::getName).forEach(fieldNames::add);