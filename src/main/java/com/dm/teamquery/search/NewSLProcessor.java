package com.dm.teamquery.search;

import lombok.SneakyThrows;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.util.Assert;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dm.teamquery.search.SearchTerm.Types;
import static com.dm.teamquery.search.SearchTerm.Types.KEYWORD;
import static com.dm.teamquery.search.SearchTerm.Types.QUOTED;
import static java.util.Arrays.stream;

public class NewSLProcessor {

    private static final String KEYWORD_SEARCH = "\\S+\\s*:\\s*\".*?\"|\\S+\\s*:\\s*\\S*";
    private static final String QUOTE_SEARCH = "(?<=\\(|^|\\s)((?<!\\\\)\").+?((?<!\\\\)\")(?=$|\\s|\\))";
    private static final String ESCAPED_CHARS = "\\ + - ! { } [ ] ^ ? /";

    private static final String END_BOOL = "(\\s*(AND|OR|NOT)\\s*)";
    private static final String SKIP_BOOL = String.format("^%s*|%<s*$", END_BOOL);

    private String query;
    private int minFuzzyLen = 3;
    private Map<String, SearchTerm> terms;

    public NewSLProcessor(int fuzziness) {
        this.minFuzzyLen = fuzziness;
    }
    public NewSLProcessor(){}

    public String format(String originalQuery) throws ParseException {
        Assert.notNull(originalQuery, "Query must not be null");
        query = originalQuery;
        query = query.replaceAll(SKIP_BOOL, "").trim();
     //   if (query.isEmpty()) return "*";

        query = "a : \"aaa bbb\" b c d e \" f g h\"";

        query = newFindEncode(QUOTE_SEARCH, query, false);
        query = newFindEncode(KEYWORD_SEARCH, query, true);

        String finalOut = decodeString(query);

        terms = new HashMap<>();
//        process();
        return query;
    }

    private String normalizeKeyword (String s){

        String k = s.split(":")[0].trim();
        if (k.isEmpty() || s.replace(":","").trim().isEmpty()) {
            return s.trim();
        }
        s = s.replaceFirst("\\s*" + Pattern.quote(k) + "\\s*:\\s*", k + ":").trim();
        return s.replace(Pattern.quote(k),k);
    }

    private void newprocess() throws ParseException{

        findAndEncode(QUOTE_SEARCH, QUOTED);
        findAndEncode(KEYWORD_SEARCH, KEYWORD);
        decode();
    }

    private String newFindEncode(String regex, String target, boolean isKeyword) throws ParseException{

        Matcher m = Pattern.compile(regex).matcher(target);
        while (m.find()) {
            target = newEncodeTerm(m.group(),  target, isKeyword);
        }

        return target;
    }

    private String newEncodeTerm(String term, String target, boolean isKeyword) throws ParseException{
        try {

            String decoded = decodeString(term);
            target = target.replace(term, decoded);

            if (isKeyword){
                String original = decoded;
                decoded =  normalizeKeyword(decoded);
                target = target.replace(original, decoded);
            }

            target = target.replace(decoded, encodeString(decoded));

            return target;
        } catch (Exception e) {throw new ParseException("Error encoding term: " + term);}
    }


    @SneakyThrows
    private String encodeString(String s){

        //return Base64.getUrlEncoder().encodeToString(s.getBytes());

        return URLEncoder.encode(s, "UTF-8");

      //  return Base64.getEncoder().withoutPadding().encodeToString(s.getBytes());
    }

    private String decodePhrase(String s){
        return stream(s.split("\\s*"))
        .map(this::decodeString)
        .collect(Collectors.joining(" "));
    }

    @SneakyThrows
    private String decodeString(String s){
      return URLDecoder.decode(s, "UTF-8");
        //  return new String(Base64.getDecoder().decode(s));
      //  return new String(Base64.getUrlDecoder().decode(s));
    }



    private void decode() {
        query = query.replaceAll("\\)", " )");
        StringBuilder sb = new StringBuilder();
        stream(query.split("\\s+")).map(this::addTermSuffix).forEach(sb::append);
    //    query = revertString(sb.toString().replaceAll(" \\)", ")"));
        for (String c : ESCAPED_CHARS.split(" ")) {
            query = query.replace(c, "\\" + c);
        }
        query = query.replaceAll("\\\\\"", "\\\"");
    }

    private String addTermSuffix(String term) {
        Matcher m = Pattern.compile("[A-Za-z0-9\"&%#@<>;`_,.](?=$)").matcher(term);
        if (isBool(term) && m.find() && term.length() > minFuzzyLen) term += "~";
        return term + " ";
    }

    private boolean isBool(String s) {
        return s.equals("AND") || s.equals("OR") || s.equals("NOT");
    }

    public void setMinFuzzyLen(int minFuzzyLen) {
        this.minFuzzyLen = minFuzzyLen;
    }
}
