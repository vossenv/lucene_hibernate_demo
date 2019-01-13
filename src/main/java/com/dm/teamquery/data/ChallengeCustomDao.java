package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Service
public class ChallengeCustomDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static String colQuery;
    private final static String rootQuery = "from Challenge where ";
    private final static String SQLKey = "SQLQuery";

    private List<String> keyWords = new ArrayList<>();

    @PostConstruct
    private void buildSearchKeywordList() {
        asList(Challenge.class.getDeclaredFields()).forEach(f -> keyWords.add(f.getName()));
        colQuery = String.join(" like ? or ", keyWords) + " like ?";
    }

    private static String surround(String s) {
        return "\'%" + s.toLowerCase() + "%\'";
    }

    public String generateQuery(Map<String, List<String>> searchMap) {

        searchMap.put(SQLKey, new ArrayList<>(asList(rootQuery)));
        Iterator<String> tIt = searchMap.get("terms").iterator();

        while (tIt.hasNext()) {
            String newQuery = searchMap.get(SQLKey).get(0) + colQuery.replace("?",
                    surround(tIt.next())) + (tIt.hasNext() ? " or " : "");

            searchMap.get(SQLKey).add(0,newQuery);
        }



        return searchMap.get(SQLKey).get(0);
    }

    public List<Challenge> searchChallenges(Map<String, List<String>> searchMap) {
        return executeSearch(generateQuery(searchMap));
    }

    public List<Challenge> executeSearch(String query) {
        return (List<Challenge>) entityManager.createQuery(query).getResultList();
    }

}
