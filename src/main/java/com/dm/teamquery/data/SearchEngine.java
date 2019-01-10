package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchEngine {


    @Inject
    ChallengeRepository challengeRepository;

    private final static String quotedSearchPattern = "((?<=author)((=|\\s=|=\\s|\\s=\\s|)\").*?(\"))|((?<=author)((=|\\s=|=\\s|\\s=\\s|)\\s).*?(\\s))";
//    private final static String quotedSearchPattern = "(?<=\").*?(?=\")";

    public List<Challenge> searchChallenges(String terms, Pageable pageable){
        return executeSearch(extractSearchTerms(terms), pageable);
    }

    private Set<String> extractSearchTerms(String terms){

        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(quotedSearchPattern).matcher(terms);
        while (m.find()) termList.add(m.group().replaceAll("\"","").replaceAll("=","").trim());
       // while (m.find()) termList.add(m.group().trim());

        termList.addAll(Arrays.asList(terms
                .replaceAll(quotedSearchPattern,"")
                .replace("\"","")
                .split("\\s")));
//
//        author= " someone new" "or" e'se
//        author="x"
//
//        author = som " kind o
//
//
//
//
//
//        ((?<=author)((=|\s=|=\s|\s=\s|)\").*(\"))|((?<=author)((=|\s=|=\s|\s=\s|)\s).*?(\s))
//
//        ((?<=author)(=|\s=|=\s|\s=\s|)(\"))?(?(1)((?<=\").*?(?=\"))|(?<=(=\s)).*?(?=\s))


        return termList;
    }

    private List<Challenge> executeSearch(Set<String> terms, Pageable pageable){
        Set<Challenge> results = new HashSet<>();
        terms.forEach((t) -> results.addAll(challengeRepository.search("%" + t.toLowerCase()+"%", pageable).getContent()));
        return new ArrayList<>(results);
    }
}
