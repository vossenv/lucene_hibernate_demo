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
    private final static String OR_OPERATOR = "OR";
    private final static String AND_OPERATOR = "\\+";
    private final static String quotedSearchPattern = "\".*?\"";
    private final static String keyTermQuoted = "(field\\s?=\\s?\").*?\"";
    private final static String keyTermUnQuoted = "(field\\s?=\\s?.).*?(?=\\s)";

    @PostConstruct
    private void buildSearchKeywordList() {
        asList(Challenge.class.getDeclaredFields()).forEach(f -> keyWords.add(f.getName().toLowerCase()));
    }

    public List<Challenge> searchChallenges(String query, Pageable pageable) {

        Map<String, List<String>> searchPatterns = constructSearchMap(query);

        return null;

    }

    private Map<String, List<String>> constructSearchMap(String query){

        Map<String, List<String>> searchPatterns = new HashMap<>();
        searchPatterns.put("and", new ArrayList<>());
        searchPatterns.put("or", new ArrayList<>());
        searchPatterns.put("toProcess", new ArrayList<>());
        searchPatterns.put("query", new ArrayList<>(asList(query)));
        
        extractKeywordValues(searchPatterns, keyTermQuoted);
        extractKeywordValues(searchPatterns, keyTermUnQuoted);

        booleanFilter(query, searchPatterns);
        standardFilter(searchPatterns);

        return  searchPatterns;

    }

    private void standardFilter(Map<String, List<String>> searchPatterns){

        match(quotedSearchPattern,searchPatterns.get("toProcess").get(0)).forEach(m -> {

            searchPatterns.get("or").add(m.replaceAll("\"",""));
            searchPatterns.get("toProcess").add( 0, searchPatterns.get("toProcess").get(0).replace(m,""));

        });

        searchPatterns.get("or").addAll(stream( searchPatterns.get("toProcess")
                .get(0).split("\\s"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList()));

        System.out.println();

    }

    private void booleanFilter(String query, Map<String, List<String>> searchPatterns) {
        asList(query.split(OR_OPERATOR)).forEach(p -> {
            String[] adds = (p.trim()).split(AND_OPERATOR);
            if (adds.length > 1)
                searchPatterns.get("and").add(stream(adds).map(String::trim).collect(Collectors.joining("+"))); else
                searchPatterns.get("toProcess").add(p);

        });
    }

    private List<String> match(String pattern, String text) {
        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group().trim());
        return new LinkedList<>(termList);
    }



    //    private final static String partialQuote = "\\S*?\".*?\"";
//    private final static String keyTermQuoted = "(field\\s?=\\s?\").*?\"";
//    private final static String keyTermUnQuoted = "(field\\s?=\\s?.).*?(?=\\s)";
//    private final static String quotedSearchPattern = "(?<=\").*?(?=\")";

//    private List<Challenge> executeSearch(Set<String> terms, Pageable pageable) {
//        Set<Challenge> results = new LinkedHashSet<>();
//        terms.forEach((t) -> results.addAll(challengeRepository.search("%" + t.toLowerCase() + "%", pageable).getContent()));
//        return new LinkedList<>(results);
//    }

    //    private Map<String, List<String>> BuildSearchMap(String query) {
//
//        Map<String, List<String>> searchPatterns = new HashMap<>();
//        searchPatterns.put("query", new ArrayList<>(asList(query)));
//
//        extractKeywordValues(searchPatterns, keyTermQuoted);
//        extractKeywordValues(searchPatterns, keyTermUnQuoted);
//        return searchPatterns;
//    }
//
    private void extractKeywordValues(Map<String, List<String>> searchPatterns, String pattern) {

        String query = searchPatterns.get("query").get(0);
        keyWords.forEach(k -> match(pattern.replace("field", k), query).forEach(m -> {
            String val = m.replaceAll("(" + k + "\\s*=)", "").replaceAll("\"", "").trim();
            if (!searchPatterns.containsKey(k)) searchPatterns.put(k, new ArrayList<>());
            searchPatterns.get(k).add(val);
            searchPatterns.get("query").add(0, searchPatterns.get("query").get(0).replace(m, ""));
        }));
    }


//    private String stripMatches(List<String> toRemove, String text){
//        return toRemove.stream().reduce(text, (s, m) -> s.replace(m, ""));
//    }

    //        termList.addAll(Arrays.asList(terms
//                .replaceAll(quotedSearchPattern,"")
//                .replace("\"","")
//                .split("\\s")));

}
