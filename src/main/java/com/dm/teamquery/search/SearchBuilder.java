package com.dm.teamquery.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import static com.dm.teamquery.search.TermTypes.AND;
import static com.dm.teamquery.search.TermTypes.OR;


@NoArgsConstructor
@Getter
@Setter
public class SearchBuilder {

    private static final String WHITESPACE = "(?<=\\S)\\s+(?=\\S)";
    private static final String QUOTE_SEARCH = "(?<=^|\\s)((?<!\\\\)\").+?((?<!\\\\)\")(?=$|\\s)";
    private static final String KEYWORD_SEARCH = "\\S+\\s*=\\s*\".*?\"|\\S+\\s*=\\s*\\S*";
    private static final String END_BOOL = String.format("(\\s*(%s|%s)\\s*)", AND.name, OR.name);
    private static final String SKIP_BOOL = String.format("^%s*|%<s*$", END_BOOL);
    public static final String AND_SEARCH = WHITESPACE + AND.name + WHITESPACE;
    private static final String OR_SEARCH = WHITESPACE + OR.name + WHITESPACE;

    @Getter
    private SLProcessor SLProcessor;

    @Getter
    @Setter
    private Class entityType = Object.class;

    @Getter
    @Setter
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

    private void init(Class entityType, String query) {
        Assert.notNull(entityType, "Invalid entity type: null");
        Assert.notNull(query, "Invalid query: null");
        this.entityType = entityType;
        this.SLProcessor = parseQuery(query);
    }

    private SLProcessor parseQuery(String queryString) {
        SLProcessor group = new SLProcessor(queryString.replaceAll(SKIP_BOOL, ""));
        group.findAndEncode(QUOTE_SEARCH, TermTypes.QUOTED);
        group.findAndEncode(KEYWORD_SEARCH, TermTypes.KEYWORD);
        group.findAndEncode(OR_SEARCH, TermTypes.OR);
        group.findAndEncode(AND_SEARCH, AND);
        group.findAndEncode(WHITESPACE, AND);
        group.encodeRemainingTermsAndIndex();
        return group;
    }
}

