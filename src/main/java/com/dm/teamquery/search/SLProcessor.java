package com.dm.teamquery.search;

import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dm.teamquery.search.TermTypes.*;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.wrap;


@NoArgsConstructor
public class SLProcessor {

    private String query;
    private Map<String, SearchTerm> terms = new HashMap<>();

    private static final String WHITESPACE = "(?<=\\S)\\s+(?=\\S)";
    private static final String QUOTE_SEARCH = "(?<=^|\\s)((?<!\\\\)\").+?((?<!\\\\)\")(?=$|\\s)";
    private static final String KEYWORD_SEARCH = "\\S+\\s*=\\s*\".*?\"|\\S+\\s*=\\s*\\S*";
    private static final String AND_SEARCH = WHITESPACE + AND.name + WHITESPACE;
    private static final String OR_SEARCH = WHITESPACE + OR.name + WHITESPACE;


    public Query analyze(String query){

        this.query = query;
        terms.clear();

        findAndEncode(QUOTE_SEARCH, QUOTED);
        findAndEncode(KEYWORD_SEARCH, KEYWORD);
        findAndEncode(OR_SEARCH, OR);
        findAndEncode(AND_SEARCH, AND);
        findAndEncode(WHITESPACE, AND);
        encodeRemaining();

        indexTerms();
        return new Query(query, terms);
    }

    public void encodeTerm(TermTypes type, String s, int loc) {
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

    public void findAndEncode(String regex, TermTypes type) {
        int offset = 0;
        Matcher m = Pattern.compile(regex).matcher(query);
        while (m.find()) {
            encodeTerm(type, m.group(), offset + m.start());
            offset = offset + (11 - m.group().length());
        }

        if (type == QUOTED)
            query = splitQuery()
                    .replaceAll("(?<!\\\\)\"","")
                    .replaceAll("\\\\\"", "\"");

    }

    private void removeKey(String id) {
        query = query.replace(id, terms.get(id).getValue());
        terms.remove(id);
    }

    private String revertString(String str){
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
