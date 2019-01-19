package com.dm.teamquery.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;


@EqualsAndHashCode
@NoArgsConstructor
public class QueryGenerator {

    @Getter @Setter private Set<String> fieldNames;
    @Getter @Setter private Class entityType;
    @Getter @Setter private String colQuery;

    @Getter @Setter private String AND_HOLDER = "%%";
    @Getter @Setter private String OR_OPERATOR = "OR";
    @Getter @Setter private String AND_OPERATOR = "AND";

    public QueryGenerator(Class entityType) {
        this.entityType = entityType;
        this.fieldNames = stream(entityType.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
        this.colQuery = String.join(" like ? or ", this.fieldNames) + " like ?";
    }

    public String generateQuery(Set<String> searchTerms){

        String result = "from " + entityType.getSimpleName();
        searchTerms = validateSearchTerms(searchTerms);

        if (!searchTerms.isEmpty()) {
            result += " where " + searchTerms.stream().map(this::getFieldString).collect(Collectors.joining(" or "));
        }

        return trimAndOr(result);
    }

    private Set<String> validateSearchTerms(Set<String> searchTerms) {
        return searchTerms.stream()
                .filter(s -> !(s
                        .replaceAll(AND_HOLDER, "")
                        .replaceAll(AND_OPERATOR, "")
                        .replaceAll(OR_OPERATOR, "")
                        .trim().isEmpty()))
                .collect(Collectors.toSet());
    }

    private String getFieldString(String s){

        StringBuilder query = new StringBuilder();
        Set<String> terms = asSet(s.split(AND_HOLDER));

        if (terms.size() == 1){
            String [] keys = s.split("=");
            return (isKeyTerm(s) && keys.length > 1) ? keys[0] + " like " + surround(keys[1]) : colQuery.replace("?", surround(s));
        }

        final Set<String> keyTerms = terms.stream().filter(this::isKeyTerm).collect(Collectors.toSet());
        final Set<String> normalTerms = terms.stream().filter(t -> !isKeyTerm(t)).collect(Collectors.toSet());

        String keysQuery = "(" + keyTerms
                .stream()
                .map(kw -> kw.split("=")[0] + " like " + surround(kw.split("=")[1]))
                .collect(Collectors.joining(" and ")) + ")";

        fieldNames.forEach(f -> {

            String normalAnd = "(";

            for (String t : normalTerms) {
                normalAnd += f + " like " + surround(t) + " and ";
            }

            query.append(trimAndOr(normalAnd)).append(") or ");

        });

        String result = trimAndOr(query.toString());
        result = (keyTerms.isEmpty()) ? result : keysQuery + " and (" + result + ") ";

        return result;
    }


    private static String surround(String s) {
        return "\'%" + s.toLowerCase() + "%\'";
    }

    private String trimAndOr(String text) {
        return text.replaceAll("\\s*(and)\\s*$", "").replaceAll("\\s*(or)\\s*$", "");
    }

    private boolean isKeyTerm(String term) {
        String[] parts = term.split("=");
        return parts.length > 0 && fieldNames.contains(parts[0].trim());
    }

}
