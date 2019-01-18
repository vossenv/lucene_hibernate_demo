package com.dm.teamquery.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@EqualsAndHashCode
@NoArgsConstructor
@Getter @Setter
public class QueryGenerator {


    private Set<String> fieldNames;
    private Class entityType;

    public QueryGenerator(Class entityType){
        this.entityType = entityType;

        this.fieldNames = Arrays
                .stream(entityType.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    public String generateQuery(Set<String> searchTerms){




        return "";
    }




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
////
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

    private static String surround(String s) {
        return "\'%" + s.toLowerCase() + "%\'";
    }

    private String trimAndOr(String text){
        return text.replaceAll("\\s*(and)\\s*$","").replaceAll("\\s*(or)\\s*$","");
    }

    private boolean isKeyTerm(String term) {
        String [] parts = term.split("=");
        return parts.length > 0 && fieldNames.contains(parts[0].trim());
    }

}
