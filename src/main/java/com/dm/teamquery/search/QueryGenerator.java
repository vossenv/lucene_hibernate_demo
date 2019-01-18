package com.dm.teamquery.search;

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
public class QueryGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    private final static String SQLKey = "SQLQuery";
    private final static  List<String> fieldNames = null;

//    private String generateAndQuery(String term){
//
//        StringBuilder query = new StringBuilder();
//
//        QueryGenerator.fieldNames.forEach(kw -> {
//            String newTerm="(";
//
//            for (String t : term.split(Search.AND_OPERATOR)) {
//                String[] keys = isKeyTerm(t) ? t.split("=") : new String[] {kw, t};
//                newTerm += keys[0] + " like " + surround(keys[1]) + " and ";
//            }
//
//            query.append(trimAndOr(newTerm)).append(") or ");
//        });
//        return query.toString();
//    }
//
//    public String generateQuery(Map<String, List<String>> searchMap) {
//
//        String colQuery = String.join(" like ? or ", fieldNames) + " like ?";
//        searchMap.put(SQLKey, new ArrayList<>(asList("from Challenge where ")));
//        Iterator<String> tIt = null; //searchMap.get(Search.termKey).iterator();
//
//        while (tIt.hasNext()) {
//
//            String t = tIt.next();
//            String newQuery = searchMap.get(SQLKey).get(0);
//
//            if (t.contains(Search.AND_OPERATOR)) {
//                newQuery += generateAndQuery(t);
//            } else {
//
//                if (isKeyTerm(t)){
//                    String [] parts = t.split("=");
//                    newQuery += parts[0] + " like " + surround(parts[1].trim()) + (tIt.hasNext() ? " or " : "");
//                } else {
//                    newQuery += colQuery.replace("?", surround(t)) + (tIt.hasNext() ? " or " : "");
//                }
//            }
//            searchMap.get(SQLKey).add(0,newQuery);
//        }
//
//        return trimAndOr(searchMap.get(SQLKey).get(0));
//
//
//    }
//
//    public List<Challenge> searchChallenges(Map<String, List<String>> searchMap) {
//        return executeSearch(generateQuery(searchMap));
//    }
//
//    public List<Challenge> executeSearch(String query) {
//        return (List<Challenge>) entityManager.createQuery(query).getResultList();
//    }
//
//    private static String surround(String s) {
//        return "\'%" + s.toLowerCase() + "%\'";
//    }
//
//    private String trimAndOr(String text){
//        return text.replaceAll("\\s*(and)\\s*$","").replaceAll("\\s*(or)\\s*$","");
//    }
//
//    private boolean isKeyTerm(String term) {
//        String [] parts = term.split("=");
//        return parts.length > 0 && fieldNames.contains(parts[0].trim());
//    }

}
