package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Getter
@Setter
public class SearchGroup {

    private Map<String, SearchTerm> mappedTerms = new HashMap<>();
    private String currentQuery;
    private String originalQuery;
    private boolean isIndexed = false;

    public SearchGroup(String currentQuery) {
        this.currentQuery = currentQuery;
        this.originalQuery = currentQuery;
    }

    public void encodeTerm(TermTypes type, String s) {
        if (!StringUtils.isEmpty(s)) {
            SearchTerm st = new SearchTerm(type, s);
            mappedTerms.put(st.getId(), st);
            currentQuery = currentQuery.replaceFirst(Pattern.quote(s), st.getId());
        }
    }

    public void encodeRemainingTermsAndIndex() {
        currentQuery = splitRawQuery();
        getTermsAsList().stream()
                .filter(k -> !mappedTerms.containsKey(k) && !k.isEmpty())
                .forEach(t -> encodeTerm(TermTypes.TEXT, findRegularTerm(t)));
        indexTerms();
    }

    private String splitRawQuery() {
        StringBuilder sb = new StringBuilder(currentQuery);
        mappedTerms.keySet().forEach(k -> sb.replace(sb.indexOf(k),
                sb.indexOf(k) + k.length(), StringUtils.wrap(k," ")));
        return sb.toString().trim().replaceAll("\\s+", " ");
    }

    private void indexTerms() {
        final AtomicInteger count = new AtomicInteger();
        getTermsAsList().forEach(t -> mappedTerms.get(t).setIndex(count.getAndIncrement()));
        isIndexed = true;
        getUpdatedQuery();
    }

    private String constructQuery(Function<SearchTerm, String> mapper, String delimiter) {
        Assert.isTrue(isIndexed, "Construct query called before indexing - remaining terms must be encoded first!");
        return mappedTerms.values().stream()
                .sorted(Comparator.comparing(SearchTerm::getIndex))
                .map(mapper)
                .collect(Collectors.joining(delimiter));
    }

    public String getUpdatedQuery() {
        this.currentQuery = constructQuery(SearchTerm::getId, "");
        return currentQuery;
    }

    public String getSplitQuery() {
        return constructQuery(SearchTerm::getId, " ");
    }

    public String getDecodedQuery() {
        return constructQuery(t -> StringUtils.wrap(t.getValue(), t.getType() == TermTypes.QUOTED ? "\"" : ""), " ");
    }

    public String getDebugQuery() {
        return constructQuery(t -> "(" + t.getIndex() + ")" + t.getType().toString() + "-{" + t.getValue() + "}", " ");
    }

    private String findRegularTerm(String r) {
        Matcher m = Pattern.compile("(^|\\s)" + r + "(\\s|$)").matcher(currentQuery);
        return m.find() ? m.group(0) : "";
    }

    private List<String> getTermsAsList() {
        return StringUtils.isEmpty(currentQuery) ? new ArrayList<>() : asList(splitRawQuery().split(" "));
    }

}
