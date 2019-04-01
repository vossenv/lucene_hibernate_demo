package com.dm.teamquery.search;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dm.teamquery.search.SearchTerm.Types;
import static com.dm.teamquery.search.SearchTerm.Types.KEYWORD;
import static com.dm.teamquery.search.SearchTerm.Types.QUOTED;
import static java.util.Arrays.stream;

public class SLProcessor {

    private static final String KEYWORD_SEARCH = "\\S+\\s*:\\s*\".*?\"|\\S+\\s*:\\s*\\S*";
    private static final String QUOTE_SEARCH = "(?<=^|\\s)((?<!\\\\)\").+?((?<!\\\\)\")(?=$|\\s)";
    private static final String ESCAPED_CHARS = "\\ + - ! { } [ ] ^ ~ ? /";

    private static final String END_BOOL = "(\\s*(AND|OR)\\s*)" ;
    private static final String SKIP_BOOL = String.format("^%s*|%<s*$", END_BOOL);

    private String query;
    private Map<String, SearchTerm> terms;

    public String format(String originalQuery) {

        query = originalQuery.trim();
        terms = new HashMap<>();
        if (query.isEmpty()) return "*";

        query = query.replaceAll(SKIP_BOOL, "");

        findAndEncode(QUOTE_SEARCH, QUOTED);
        findAndEncode(KEYWORD_SEARCH, KEYWORD);

        for (String c : ESCAPED_CHARS.split(" ")) {
            query = query.replace(c, "\\" + c);
        }
        return decode();
    }

    private String decode(){
        StringBuilder sb = new StringBuilder();
        stream(query.split("\\s+")).map(s -> isBool(s) || s.isEmpty() ? s + " " : s + "~ ").forEach(sb::append);
        return revertString(sb.toString());
    }

    private boolean isBool(String s) {
        return s.equals("AND") || s.equals("OR") || s.equals("NOT");
    }

    public void encodeTerm(Types type, String s, int loc) {
        if (type == KEYWORD) s = revertString(s);
        SearchTerm st = new SearchTerm(type, s);
        terms.put(st.getId(), st);
        query = new StringBuilder(query)
                .replace(loc, loc + s.length(), st.getId())
                .toString();
    }

    public void findAndEncode(String regex, Types type) {
        int offset = 0;
        Matcher m = Pattern.compile(regex).matcher(query);
        while (m.find()) {
            encodeTerm(type, m.group(), offset + m.start());
            offset = offset + (SearchTerm.idLength - m.group().length());
        }
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
        return str.trim();
    }


}
