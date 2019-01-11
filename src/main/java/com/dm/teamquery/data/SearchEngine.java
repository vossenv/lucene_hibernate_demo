package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

@Service
public class SearchEngine {


    @Inject
    ChallengeRepository challengeRepository;

    private List<String> keyWords = new ArrayList<>();
    private final static String queryKey = "OriginalQuery";
    private final static String keyTermQuoted = "(field\\s?=\\s?\").*?\"";
    private final static String keyTermUnQuoted = "(field\\s?=\\s?.).*?(?=\\s)";



//    private final static String quotedSearchPattern = "((?<=author)((=|\\s=|=\\s|\\s=\\s|)\").*?(\"))|((?<=author)((=|\\s=|=\\s|\\s=\\s|)\\s).*?(\\s))";
//    private final static String quotedSearchPattern = "(?<=\").*?(?=\")";


    @PostConstruct
    private void buildSearchKeywordList() {
        asList(Challenge.class.getDeclaredFields()).forEach(f -> keyWords.add(f.getName().toLowerCase()));
    }

    public List<Challenge> searchChallenges(String query, Pageable pageable) {
        BuildSearchMap(query)
        return  null;
        //   return executeSearch(BuildSearchMap(query), pageable);
    }

    private Map<String, List<String>>  BuildSearchMap(String query) {

        Map<String, List<String>> keyTerms = new HashMap<>();
        keyTerms.put(queryKey, new ArrayList<>(asList(query)));

        extractKeywordValues(keyTerms, keyTermQuoted);
        extractKeywordValues(keyTerms, keyTermUnQuoted);
        return keyTerms;
    }

    private void extractKeywordValues(Map<String, List<String>> keyTerms, String pattern) {

        String query = keyTerms.get(queryKey).get(0);
        keyWords.forEach(k -> match(pattern.replace("field", k), query).forEach(m -> {
            String val = m.replaceAll("(" + k + "\\s*=)", "").replaceAll("\"", "").trim();
            if (!keyTerms.containsKey(k)) keyTerms.put(k, new ArrayList<>());
            keyTerms.get(k).add(val);
            keyTerms.get(queryKey).add(0,keyTerms.get(queryKey).get(0).replace(m, ""));
        }));
    }

    private List<Challenge> executeSearch(Set<String> terms, Pageable pageable) {
        Set<Challenge> results = new HashSet<>();
        terms.forEach((t) -> results.addAll(challengeRepository.search("%" + t.toLowerCase() + "%", pageable).getContent()));
        return new ArrayList<>(results);
    }

    private List<String> match(String pattern, String text) {
        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group().trim());
        return new ArrayList<>(termList);
    }

//    private String stripMatches(List<String> toRemove, String text){
//        return toRemove.stream().reduce(text, (s, m) -> s.replace(m, ""));
//    }

    //        termList.addAll(Arrays.asList(terms
//                .replaceAll(quotedSearchPattern,"")
//                .replace("\"","")
//                .split("\\s")));

}
