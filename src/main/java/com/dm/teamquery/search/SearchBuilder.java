package com.dm.teamquery.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

@NoArgsConstructor
public class SearchBuilder {

    private static final String WHITESPACE = "(?<=\\S)\\s+(?=\\S)";
    private static final String QUOTE_SEARCH = "(?<=^|\\s)((?<!\\\\)\").*?((?<!\\\\)\")(?=$|\\s)";
    private static final String KEYWORD_SEARCH = "\\S*\\s*=\\s*\".*?\"|\\S*\\s*=\\s*\\S*";
    private static final String AND_SEARCH = WHITESPACE + TermTypes.AND.name + WHITESPACE;
    private static final String OR_SEARCH = WHITESPACE + TermTypes.OR.name + WHITESPACE;

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

    public SearchBuilder(Class entityType) { init(entityType, ""); }

    public SearchBuilder setQuery(String query) {
        init(this.entityType, query);
        return this;
    }

    private void init(Class entityType, String query){
        Assert.notNull(entityType,"Invalid entity type: null");
        Assert.notNull(query,"Invalid query: null");
        this.entityType = entityType;
        this.searchGroup = parseQuery(query);
    }

    private SearchGroup parseQuery(String queryString) {
        SearchGroup group = new SearchGroup(queryString);
        group.findAndEncode(QUOTE_SEARCH, TermTypes.QUOTED);
        group.findAndEncode(KEYWORD_SEARCH, TermTypes.KEYWORD);
        group.findAndEncode(OR_SEARCH, TermTypes.OR);
        group.findAndEncode(AND_SEARCH, TermTypes.AND);
        group.findAndEncode(WHITESPACE, TermTypes.AND);
        group.encodeRemainingTermsAndIndex();
        return group;
    }
}

