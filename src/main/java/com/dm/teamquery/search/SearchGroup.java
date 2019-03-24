package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static com.dm.teamquery.search.TermTypes.QUOTED;
import static com.dm.teamquery.search.TermTypes.TEXT;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.wrap;

@Getter
@Setter
public class SearchGroup {

    private String query;
    private String originalQuery;
    private boolean isIndexed = false;
    private Map<String, SearchTerm> mappedTerms = new HashMap<>();

    public SearchGroup(String query) {
        this.query = query;
        this.originalQuery = query;
    }

    public void encodeTerm(TermTypes type, String s) {
        if (!isEmpty(s)) {
            SearchTerm st = new SearchTerm(type, s);
            mappedTerms.put(st.getId(), st);
            query = query.replaceFirst(Pattern.quote(s), st.getId());
        }
    }

    public void encodeRemainingTermsAndIndex() {
        query = splitQuery();
        getTermsAsList().stream()
                .filter(k -> !mappedTerms.containsKey(k) && !k.isEmpty())
                .map(t -> match("(^|\\s+)" + t + "(\\s+|$)", query).get(0))
                .forEach(t -> encodeTerm(TEXT,t));

        indexTerms();
        query = getUpdatedQuery();
    }

    private String splitQuery() {
        String cq = query;
        for (String t : mappedTerms.keySet()) cq = cq.replace(t, wrap(t, " "));
        return cq.trim();
    }

    private void indexTerms() {
        final AtomicInteger count = new AtomicInteger();
        getTermsAsList().forEach(t -> mappedTerms.get(t).setIndex(count.getAndIncrement()));
        isIndexed = true;
    }

    private String constructQuery(Function<SearchTerm, String> mapper, String delimiter) {
        Assert.isTrue(isIndexed,"Construct query called before indexing " +
                "- remaining terms must be encoded first!");
        return mappedTerms.values().stream()
                .sorted(Comparator.comparing(SearchTerm::getIndex))
                .map(mapper)
                .collect(Collectors.joining(delimiter));
    }

    private List<String> match(String pattern, String text) {
        List<String> termList = new LinkedList<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group());
        return new LinkedList<>(termList);
    }

    public void findAndEncode(String regex, TermTypes type) {
        match(regex, query).forEach(t -> encodeTerm(type, t));
    }

    public String getUpdatedQuery() {
        return constructQuery(SearchTerm::getId, "");
    }

    public String getSplitQuery() {
        return constructQuery(SearchTerm::getId, " ");
    }

    public String getDecodedQuery() {
        return constructQuery(t -> wrap(t.getValue(), t.getType() == QUOTED ? "\"" : ""), " ");
    }

    public String getDebugQuery() {
        return constructQuery(t -> "(" + t.getIndex() + ")" + t.getType().toString() + "-{" + t.getValue() + "}", " ");
    }

    private List<String> getTermsAsList() {
        return isEmpty(query) ? new ArrayList<>() : asList(splitQuery().split("\\s+"));
    }

}
