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

    private static String colQuery, andQuery;
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

    private String generateAndQuery(int size){

        StringBuilder query = new StringBuilder();
        Iterator<String> kIt = keyWords.iterator();

        while (kIt.hasNext()){
            String t = kIt.next();
            StringBuilder newQuery = new StringBuilder("(");
            for (int i = 0; i < size; i++) {
                newQuery.append(t).append(" like ").append(i).append(i == size - 2 ? " and " : ")");
            }
            query.append(newQuery).append(kIt.hasNext() ? " or " : "");
        }
        return query.toString();
    }


    public String generateQuery(Map<String, List<String>> searchMap) {

        searchMap.put(SQLKey, new ArrayList<>(asList(rootQuery)));
        Iterator<String> tIt = searchMap.get("terms").iterator();

        while (tIt.hasNext()) {

            String t = tIt.next();

            if (t.contains("AND")) {

                String q = generateAndQuery(t.split("AND").length);
            }

            String newQuery = searchMap.get(SQLKey).get(0) + colQuery.replace("?",
                    surround(t)) + (tIt.hasNext() ? " or " : "");

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
