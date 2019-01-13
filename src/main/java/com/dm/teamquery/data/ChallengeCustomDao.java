package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.stereotype.Service;

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

    private final static String SQLKey = "SQLQuery";
    private final static  List<String> keyWords = SearchEngine.keyWords;

    private String generateAndQuery(String term){

        String [] terms = term.split(SearchEngine.AND_OPERATOR);
        int size = terms.length;

        StringBuilder query = new StringBuilder();
        Iterator<String> kIt = keyWords.iterator();

        while (kIt.hasNext()){
            String t = kIt.next();
            StringBuilder newQuery = new StringBuilder("(");

            for (int i = 0; i < size; i++) {
                String [] keys = terms[i].split("=");

                if (keys.length > 1 && keys[0].trim().equals(t)){
                    newQuery.append(t).append(" like ").append(surround(keys[1].trim())).append(i < size - 1 ? " and " : ")");
                } else if (!isKeyTerm(keys[0].trim())) {
                    newQuery.append(t).append(" like ").append(surround(terms[i])).append(i < size - 1 ? " and " : ")");
                }
            }
            query.append(newQuery).append(kIt.hasNext() ? " or " : "");
        }
        return query.append(" or ").toString();
    }

    public String generateQuery(Map<String, List<String>> searchMap) {

        String colQuery = String.join(" like ? or ", keyWords) + " like ?";
        searchMap.put(SQLKey, new ArrayList<>(asList("from Challenge where ")));
        Iterator<String> tIt = searchMap.get(SearchEngine.termKey).iterator();

        while (tIt.hasNext()) {

            String t = tIt.next();
            String newQuery = searchMap.get(SQLKey).get(0);

            if (t.contains(SearchEngine.AND_OPERATOR)) {
                newQuery += generateAndQuery(t);
            } else {

                if (isKeyTerm(t)){
                    String [] parts = t.split("=");
                    newQuery += parts[0] + " like " + surround(parts[1].trim()) + (tIt.hasNext() ? " or " : "");
                } else {
                    newQuery += colQuery.replace("?", surround(t)) + (tIt.hasNext() ? " or " : "");
                }
            }
            searchMap.get(SQLKey).add(0,newQuery);
        }
        return searchMap.get(SQLKey).get(0).replaceAll("(and)\\s*$","").replaceAll("(or)\\s*$","");


    }

    public List<Challenge> searchChallenges(Map<String, List<String>> searchMap) {
        return executeSearch(generateQuery(searchMap));
    }

    public List<Challenge> executeSearch(String query) {
        return (List<Challenge>) entityManager.createQuery(query).getResultList();
    }

    private static String surround(String s) {
        return "\'%" + s.toLowerCase() + "%\'";
    }

    private boolean isKeyTerm(String term) {
        String [] parts = term.split("=");
        return parts.length > 0 && keyWords.contains(parts[0].trim());
    }

}
