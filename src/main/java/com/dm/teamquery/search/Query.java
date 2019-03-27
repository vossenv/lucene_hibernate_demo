package com.dm.teamquery.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dm.teamquery.search.SearchTerm.Types.*;
import static org.apache.commons.lang3.StringUtils.wrap;

@Getter
@AllArgsConstructor
public class Query {

    private String originalQuery;
    private Map<String, SearchTerm> mappedTerms;

    public String getUpdatedQuery() {
        return constructQuery(SearchTerm::getId, "");
    }

    public String getSplitQuery() {
        return constructQuery(SearchTerm::getId, " ");
    }

    public String getDecodedQuery() {
        return constructQuery(t -> wrap(t.getValue(), t.getType().is(QUOTED) ? "\"" : ""), " ");
    }

    public String getDebugQuery() {
        return constructQuery(t ->
                "(" + t.getIndex() + ")" + t.getType().toString() + "-{"
                        + (t.getType().is(KEYWORD) ? "key: " + t.getKey()  + ", val: ": "")
                        + t.getValue() + "}", " ");
    }

    private String constructQuery(Function<SearchTerm, String> mapper, String delimiter) {
        return mappedTerms.values().stream()
                .sorted(Comparator.comparing(SearchTerm::getIndex))
                .map(mapper)
                .collect(Collectors.joining(delimiter));
    }
}
