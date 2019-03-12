package com.dm.teamquery.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;


@EqualsAndHashCode
@NoArgsConstructor
@Getter @Setter
public class QueryGenerator {

    private Set<String> fieldNames;
    private String entityName;
    private String colQuery;
    private String AND_HOLDER = ";+@!&@";
    private String OR_HOLDER = ";=@!&@";
    private String OR_OPERATOR = "OR";
    private String AND_OPERATOR = "AND";

    public QueryGenerator(String entityName, Set<String> fieldNames) {
        this.entityName = entityName;
        this.fieldNames = fieldNames;
        this.colQuery = String.join(" like ? or ", this.fieldNames) + " like ?";
    }

    public String generateQuery(Set<String> searchTerms){

        searchTerms = validateSearchTerms(searchTerms);
        return trimConjunctions( "from "
                + entityName + " where "
                + searchTerms.stream()
                .map(this::getFieldString)
                .collect(Collectors.joining(" and ")));
    }

    private String getFieldString(String s){

        Set<String> terms = asSet(s.split(OR_HOLDER));
        StringBuilder query = new StringBuilder("(");

        if (terms.size() == 1){
            String [] keys = s.split("=");
            return (isKeyTerm(s) && keys.length > 1) ? keys[0] + " like " + surround(keys[1]) : "(" + colQuery.replace("?", surround(s)) + ")";
        }

        final Set<String> keyTerms = terms.stream().filter(this::isKeyTerm).collect(Collectors.toCollection(LinkedHashSet::new));
        final Set<String> normalTerms = terms.stream().filter(t -> !isKeyTerm(t)).collect(Collectors.toCollection(LinkedHashSet::new));

        String keysQuery =
                "(" + keyTerms.stream()
                .map(k -> k.split("=")[0] + " like " + surround(k.split("=")[1]))
                .collect(Collectors.joining(" and ")) + ")";

        if (normalTerms.size() > 0) {
            fieldNames.forEach(f -> {
                query.append("(");
                Iterator<String> nI = normalTerms.iterator();
                while (nI.hasNext()) {
                    query.append(f).append(" like ")
                            .append(surround(nI.next()))
                            .append(nI.hasNext() ? " or " : ") or ");
                }
            });

            String result = trimConjunctions(query.toString()) + ")";
            return (keyTerms.isEmpty()) ? result : keysQuery + " and (" + result + ") ";

        } else {
            return keysQuery;
        }
    }

    private Set<String> validateSearchTerms(Set<String> searchTerms) {
        return searchTerms.stream()
                .filter(s -> !(s
                        .replaceAll(OR_HOLDER, "")
                        .replaceAll(AND_OPERATOR, "")
                        .replaceAll(OR_OPERATOR, "")
                        .trim().isEmpty()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static String surround(String s) {
        return "\'%" + s.toLowerCase() + "%\'";
    }

    private String trimConjunctions(String text) {
        return text.replaceAll("\\s*(and|or|where)\\s*$", "");
    }

    private boolean isKeyTerm(String term) {
        String[] parts = term.split("=");
        return parts.length > 0 && fieldNames.contains(parts[0].trim());
    }
}
