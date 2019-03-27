package com.dm.teamquery.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dm.teamquery.search.TermTypes.*;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.wrap;

@Getter
@Setter
@NoArgsConstructor
public class SLProcessor {

    private String query;
    private String originalQuery;
    private boolean isIndexed = false;
    private Map<String, SearchTerm> mappedTerms = new HashMap<>();

    private static final String WHITESPACE = "(?<=\\S)\\s+(?=\\S)";
    private static final String QUOTE_SEARCH = "(?<=^|\\s)((?<!\\\\)\").+?((?<!\\\\)\")(?=$|\\s)";
    private static final String KEYWORD_SEARCH = "\\S+\\s*=\\s*\".*?\"|\\S+\\s*=\\s*\\S*";
    private static final String END_BOOL = String.format("(\\s*(%s|%s)\\s*)", AND.name, OR.name);
    private static final String SKIP_BOOL = String.format("^%s*|%<s*$", END_BOOL);
    private static final String AND_SEARCH = WHITESPACE + AND.name + WHITESPACE;
    private static final String OR_SEARCH = WHITESPACE + OR.name + WHITESPACE;

    public SLProcessor(String query) {
        analyze(query);
    }


    public void analyze(String query){
        this.query = query;
        originalQuery = query;
        mappedTerms = new HashMap<>();
        isIndexed = false;

        findAndEncode(QUOTE_SEARCH, TermTypes.QUOTED);
        findAndEncode(KEYWORD_SEARCH, TermTypes.KEYWORD);
        findAndEncode(OR_SEARCH, TermTypes.OR);
        findAndEncode(AND_SEARCH, AND);
        findAndEncode(WHITESPACE, AND);
        encodeRemainingTermsAndIndex();
        
    }

    

    public void encodeTerm(TermTypes type, String s, int loc) {
        if (type == KEYWORD) s = revertString(s);
        SearchTerm st = new SearchTerm(type, s);
        mappedTerms.put(st.getId(), st);
        query = new StringBuilder(query)
                .replace(loc, loc + s.length(), st.getId())
                .toString();
    }

    public void encodeRemainingTermsAndIndex() {
        query = splitQuery();
        getTermsAsList().stream()
                .filter(k -> !mappedTerms.containsKey(k) && !k.isEmpty())
                .forEach(t -> findAndEncode("(^|\\s+)" + t + "(\\s+|$)", TEXT));

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
        getTermsAsList().forEach(t ->
                mappedTerms.get(t).setIndex(count.getAndIncrement()));
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
        query = query.replace(id,mappedTerms.get(id).getValue());
        mappedTerms.remove(id);
    }

    private String revertString(String str){
        Set<String> toRevert = mappedTerms.keySet().stream()
                .filter(str::contains)
                .collect(Collectors.toSet());
        for (String s : toRevert) {
                str = str.replace(s, mappedTerms.get(s).getValue());
                removeKey(s);
        }
        return str;
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
        return constructQuery(t ->
                "(" + t.getIndex() + ")" + t.getType().toString() + "-{"
                + (t.getType() == KEYWORD ? "key: " + t.getKey()  + ", val: ": "")
                + t.getValue() + "}", " ");
    }

    private List<String> getTermsAsList() {
        return isEmpty(query) ? new ArrayList<>() : asList(splitQuery().split("\\s+"));
    }

}
