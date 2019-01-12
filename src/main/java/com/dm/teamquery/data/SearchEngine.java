package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

@Service
public class SearchEngine {


    @Inject
    ChallengeRepository challengeRepository;

    private List<String> keyWords = new ArrayList<>();
    private final static String quotedSearchPattern = "\".*?\"";
    private final static String keyTermQuoted = "(field\\s?=\\s?\").*?\"";
    private final static String keyTermUnQuoted = "(field\\s?=\\s?.).*?(?=(\\s|$))";

    private final static String OR_OPERATOR = "OR";
    private final static String AND_OPERATOR = "AND";
    private final static String queryKey = "query";
    private final static String termKey = "terms";
    private final static String processKey = "toProcess";
    private final static String andKey = "and";

    @PostConstruct
    private void buildSearchKeywordList() {
        asList(Challenge.class.getDeclaredFields()).forEach(f -> keyWords.add(f.getName().toLowerCase()));
    }

    public List<Challenge> searchChallenges(String query, Pageable pageable) {

        Map<String, List<String>> searchPatterns = constructSearchMap(query);

        return null;

    }

    public Map<String, List<String>> constructSearchMap(String query) {

        Map<String, List<String>> searchPatterns = new HashMap<>();
        searchPatterns.put(andKey, new LinkedList<>());
        searchPatterns.put(termKey, new LinkedList<>());
        searchPatterns.put(processKey, new LinkedList<>());
        searchPatterns.put(queryKey, new LinkedList<>(asList(query)));
        keyWords.forEach(k -> searchPatterns.put(k, new LinkedList<>()));

        keywordFilter(searchPatterns);
        booleanFilter(searchPatterns);
        standardFilter(searchPatterns);
        
        searchPatterns.get(termKey).addAll(searchPatterns.get(andKey));
        searchPatterns.remove(andKey);
        searchPatterns.remove(processKey);
        searchPatterns.remove(queryKey);

        return searchPatterns;

    }

    private void keywordFilter(Map<String, List<String>> searchPatterns) {

        extractKeywordValues(searchPatterns, keyTermQuoted);
        extractKeywordValues(searchPatterns, keyTermUnQuoted);
    }

    private void standardFilter(Map<String, List<String>> searchPatterns) {

        if (searchPatterns.get(processKey).isEmpty()) return;

        match(quotedSearchPattern, searchPatterns.get(processKey).get(0)).forEach(m -> {
            searchPatterns.get(termKey).add(m.replaceAll("\"", ""));
            searchPatterns.get(processKey).add(0, searchPatterns.get(processKey).get(0).replace(m, ""));
        });

        searchPatterns.get(termKey).addAll(stream(searchPatterns.get(processKey)
                .get(0).split("\\s"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList()));
    }

    private void booleanFilter(Map<String, List<String>> searchPatterns) {
        asList(searchPatterns.get(queryKey).get(0).split(OR_OPERATOR)).forEach(p -> {
            String[] adds = (p.trim()).split(AND_OPERATOR);
            if (adds.length > 1)
                searchPatterns.get(andKey).add(stream(adds).map(String::trim).collect(Collectors.joining(AND_OPERATOR)));
            else
                searchPatterns.get(processKey).add(p);
        });

        List<String> newAndList = new LinkedList<>();
        searchPatterns.get(andKey).forEach(val -> {

            if (val.matches(".*\\s+.*")){
                List<String> quoted = match(quotedSearchPattern, val);
                newAndList.addAll(quoted.stream().map(s -> s.replace("\"","")).collect(Collectors.toList()) );
                newAndList.addAll(asList(quoted.stream().reduce(val, (str, toRem) -> str.replaceAll(toRem, "")).split("\\s")));

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

    private void extractKeywordValues(Map<String, List<String>> searchPatterns, String pattern) {
        String query = searchPatterns.get(queryKey).get(0);
        keyWords.forEach(k -> match(pattern.replace("field", k), query).forEach(m -> {
            String val = m.replaceAll("(" + k + "\\s*=)", "").replaceAll("\"", "").trim();
            searchPatterns.get(k).add(val);
            searchPatterns.get(queryKey).add(0, searchPatterns.get(queryKey).get(0).replace(m, ""));
        }));
    }
}
