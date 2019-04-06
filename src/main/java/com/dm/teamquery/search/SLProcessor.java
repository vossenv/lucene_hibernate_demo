package com.dm.teamquery.search;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dm.teamquery.search.SearchTerm.Types;
import static com.dm.teamquery.search.SearchTerm.Types.*;
import static java.util.Arrays.stream;

public class SLProcessor {

    private static final String KEYWORD_SEARCH = "\\S+\\s*:\\s*\".*?\"|\\S+\\s*:\\s*\\S*";
    private static final String QUOTE_SEARCH = "(?<=\\(|^|\\s)((?<!\\\\)\").+?((?<!\\\\)\")(?=$|\\s|\\))";
    private static final String ESCAPED_CHARS = "\\ + - ! { } [ ] ^ ? /";

    private static final String END_BOOL = "(\\s*(AND|OR|NOT)\\s*)";
    private static final String SKIP_BOOL = String.format("^%s*|%<s*$", END_BOOL);

    private String query;
    private Map<String, SearchTerm> terms;

    public String format(String originalQuery) throws ParseException {
        Assert.notNull(originalQuery, "Query must not be null");
        terms = new HashMap<>();
        query = originalQuery.trim();
        if (query.isEmpty()) return "*";

        validateQuery();
        process();

        return query;
    }

    private void validateQuery() throws ParseException {
        String s = query.replaceAll("[^A-Za-z0-9*]*","");
        s = s.replaceAll("AND|OR|NOT","").trim();
        if (s.isEmpty()) throw new ParseException("Invalid query syntax: '" + query + "'");
    }

    private void process() throws ParseException{
        query = query.replaceAll(SKIP_BOOL, "");
        findAndEncode(QUOTE_SEARCH, QUOTED);
        findAndEncode(KEYWORD_SEARCH, KEYWORD);
        decode();
        validateQuery();
    }

    private void decode() {
        query = query.replaceAll("\\)", " )");
        StringBuilder sb = new StringBuilder();
        stream(query.split("\\s+")).map(this::addTermSuffix).forEach(sb::append);
        query = revertString(sb.toString().replaceAll(" \\)", ")"));
        for (String c : ESCAPED_CHARS.split(" ")) {
            query = query.replace(c, "\\" + c);
        }
        query = query.replaceAll("\\\\\"", "\\\"");
    }

    private String addTermSuffix(String term) {
        String s = (terms.containsKey(term)) ? terms.get(term).getValue() : term;
        Matcher m = Pattern.compile("[A-Za-z0-9\"&%#@<>;`_,.](?=$)").matcher(s);
        return (!SearchTerm.Types.isBoolean(s) && m.find()) ? term + "~ " : term + " ";
    }

    private void encodeTerm(Types type, String s, int loc) throws ParseException{
        if (type == KEYWORD) s = revertString(s);
        SearchTerm st = new SearchTerm(type, s);
        terms.put(st.getId(), st);
        try {
            query = new StringBuilder(query)
                    .replace(loc, loc + s.length(), st.getId())
                    .toString();
        } catch (Exception e) {throw new ParseException("Error encoding term: " + s);}
    }

    private void findAndEncode(String regex, Types type) throws ParseException{
        int offset = 0;
        Matcher m = Pattern.compile(regex).matcher(query);
        while (m.find()) {
            encodeTerm(type, m.group(), offset + m.start());
            offset = offset + (SearchTerm.idLength - m.group().length());
        }
    }


    private String revertString(String str) {
        Set<String> toRevert = terms.keySet().stream()
                .filter(str::contains)
                .collect(Collectors.toSet());
        for (String s : toRevert) {
            str = str.replace(s, terms.get(s).getValue());
            query = query.replace(s, terms.get(s).getValue());
            terms.remove(s);
        }
        return str.trim();
    }


}
