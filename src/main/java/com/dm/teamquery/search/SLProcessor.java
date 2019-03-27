package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dm.teamquery.search.SearchTerm.Types.*;
import static com.dm.teamquery.search.SearchTerm.Types;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.wrap;

public class SLProcessor {

    private static final String SPACE = "(?<=\\S)\\s+(?=\\S)";
    private static final String QUOTE_SEARCH = "(?<=^|\\s)((?<!\\\\)\").+?((?<!\\\\)\")(?=$|\\s)";
    private static final String KEYWORD_SEARCH = "\\S+\\s*=\\s*\".*?\"|\\S+\\s*=\\s*\\S*";

    @Setter
    @Getter
    private String AND_VAL = "AND";
    @Setter
    @Getter
    private String OR_VAL = "OR";

    private String query;
    private Map<String, SearchTerm> terms;

    public Query analyze(String query) {

        this.query = query;
        this.terms = new HashMap<>();

        findAndEncode(QUOTE_SEARCH, QUOTED);
        findAndEncode(KEYWORD_SEARCH, KEYWORD);
        findAndEncode(SPACE + OR_VAL + SPACE, OR);
        findAndEncode(SPACE + AND_VAL + SPACE, AND);
        findAndEncode(SPACE, AND);
        encodeRemaining();
        indexTerms();
        return new Query(query, terms);
    }

    public void encodeTerm(Types type, String s, int loc) {
        if (type == KEYWORD) s = revertString(s);
        SearchTerm st = new SearchTerm(type, s);
        terms.put(st.getId(), st);
        query = new StringBuilder(query)
                .replace(loc, loc + s.length(), st.getId())
                .toString();
    }

    public void encodeRemaining() {
        query = splitQuery();
        getTermsAsList().stream()
                .filter(k -> !terms.containsKey(k) && !k.isEmpty())
                .forEach(t -> findAndEncode("(^|\\s+)" + t + "(\\s+|$)", TEXT));
    }

    private String splitQuery() {
        String cq = query;
        for (String t : terms.keySet()) cq = cq.replace(t, wrap(t, " "));
        return cq.trim();
    }

    private void indexTerms() {
        final AtomicInteger count = new AtomicInteger();
        getTermsAsList().forEach(t -> terms.get(t).setIndex(count.getAndIncrement()));
    }

    public void findAndEncode(String regex, Types type) {
        int offset = 0;
        Matcher m = Pattern.compile(regex).matcher(query);
        while (m.find()) {
            encodeTerm(type, m.group(), offset + m.start());
            offset = offset + (SearchTerm.idLength - m.group().length());
        }

        if (type.is(QUOTED)) flattenQuotes();
    }

    private void flattenQuotes() {
        query = query
                .replaceAll("(?<!\\\\)\"", "")
                .replaceAll("\\\\\"", "\"");
    }

    private void removeKey(String id) {
        query = query.replace(id, terms.get(id).getValue());
        terms.remove(id);
    }

    private String revertString(String str) {
        Set<String> toRevert = terms.keySet().stream()
                .filter(str::contains)
                .collect(Collectors.toSet());
        for (String s : toRevert) {
            str = str.replace(s, terms.get(s).getValue());
            removeKey(s);
        }
        return str;
    }

    private List<String> getTermsAsList() {
        return isEmpty(query) ? new ArrayList<>() : asList(splitQuery().split("\\s+"));
    }

}
