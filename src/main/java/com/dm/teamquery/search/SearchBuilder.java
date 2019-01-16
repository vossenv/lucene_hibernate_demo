package com.dm.teamquery.search;

import com.dm.teamquery.model.Challenge;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

@Service
public class SearchBuilder {

    @Inject
    QueryGenerator dao;

    private final static String quotedSearchPattern = "\".*?\"";
    private final static String keyTermQuoted = "(\\S*\\s*=\\s?\").*?\"";
    private final static String keyTermQuoted2 = "(field\\s?=\\s?\").*?\"";
    public final static  List<String> keyWords = Arrays.stream(Challenge.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    public final static String termKey = "terms";
    public final static String OR_OPERATOR = "OR";
    public final static String AND_OPERATOR = "AND";
    private final static String queryKey = "query";
    private final static String processKey = "toProcess";
    private final static String andKey = "and";

    public List<Challenge> searchChallenges(String query, Pageable pageable) {

        Map<String, List<String>> searchPatterns = constructSearchMap(query);
        return dao.searchChallenges(searchPatterns);
    }

    public Map<String, List<String>> constructSearchMap(String query) {

        Map<String, List<String>> searchPatterns = new HashMap<>();
        searchPatterns.put(andKey, new LinkedList<>());
        searchPatterns.put(termKey, new LinkedList<>());
        searchPatterns.put(processKey, new LinkedList<>());
        searchPatterns.put(queryKey, new LinkedList<>(asList(query)));

        booleanFilter(searchPatterns);
        standardFilter(searchPatterns);
        
        searchPatterns.get(termKey).addAll(searchPatterns.get(andKey));
        searchPatterns.remove(andKey);
        searchPatterns.remove(processKey);
        searchPatterns.remove(queryKey);

        return searchPatterns;
    }

    private void standardFilter(Map<String, List<String>> searchPatterns) {

        if (searchPatterns.get(processKey).isEmpty()) return;

        List<String> keyQuoted = match(keyTermQuoted, searchPatterns.get(processKey).get(0));

        keyQuoted.forEach(kq -> {
            searchPatterns.get(termKey).add(kq.replaceAll("\"", ""));
            searchPatterns.get(processKey).add(0, searchPatterns.get(processKey).get(0).replace(kq, ""));
        });

        match(quotedSearchPattern, searchPatterns.get(processKey).get(0)).forEach(m -> {
            searchPatterns.get(termKey).add(m.replaceAll("\"", ""));
            searchPatterns.get(processKey).add(0, searchPatterns.get(processKey).get(0).replace(m, ""));
        });

        searchPatterns.get(termKey).addAll(searchPatterns.get(processKey).stream()
                .map(s -> s.split("\\s"))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList()));

//        searchPatterns.get(termKey).addAll(stream(searchPatterns.get(processKey)
//                .get(0).split("\\s"))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .collect(Collectors.toList()));
    }

    private String strip (String string, String term){
        return  string.replaceAll(term, term.replaceAll("\\s+","__").replace("\"",""));
    }

    private List<String> unStrip(String string){
        return Arrays.stream(string.split("\\s")).map(s -> s.replaceAll("__"," ")).collect(Collectors.toList());
    }


    private void booleanFilter(Map<String, List<String>> searchPatterns) {
        asList(searchPatterns.get(queryKey).get(0).split(OR_OPERATOR)).forEach(p -> {
            String[] adds = (p.trim()).split(AND_OPERATOR);
            if (adds.length > 1)
                searchPatterns.get(andKey).add(stream(adds).map(String::trim).collect(Collectors.joining(AND_OPERATOR)));
            else
                searchPatterns.get(processKey).add(p.trim());
        });

        List<String> newAndList = new LinkedList<>();

        searchPatterns.get(andKey).forEach(val -> {
            if (val.matches(".*\\s+.*")){
                List<String> keyQuoted = match(keyTermQuoted, val);
                List<String> quoted = match(quotedSearchPattern, val);
                for (String q : keyQuoted) val = strip(val, q);
                for (String q : quoted) val = strip(val, q);
                newAndList.addAll(unStrip(val));
            } else {
                newAndList.add(val);
            }
        });

        searchPatterns.put(andKey, newAndList.stream().filter(v -> !v.isEmpty()).collect(Collectors.toList()));
    }



    private List<String> match(String pattern, String text) {
        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group().trim());
        return new LinkedList<>(termList);
    }

}
