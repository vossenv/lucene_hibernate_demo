package com.dm.teamquery.search;


import com.dm.teamquery.execption.customexception.TeamQueryException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

@EqualsAndHashCode
@NoArgsConstructor
public class Search2 {

    @Getter private final String AND_OPERATOR = "AND";
    @Getter private final String OR_OPERATOR = "OR";
    @Getter @Setter private Set<String> fieldNames;

    @Getter
    private final String OR_HOLDER = ";=@!&@";
    private final String SPACE_HOLDER = "!a#!%";
    private final String TAB_HOLDER = "@&%*";
    private final String TERM_SEPARATOR = "#`]//:";

    private final String QUOTE_SEARCH = "\".*\"";
    private final String badKeyTerms = "(\\S*\\s*=\\s*$|^\\s*=\\S*)";
    private final String specialTerms = "(\\S*\\s*=\\s*\".*?\"|\\S*\\s*=\\s*\\S*|\".*?\"|\\S*\\s*=\\s*\".*\"|\\S*\\s*=\\s*.*?(?=\\s))";
    private final String andSearchPattern = "(?<=\\S)\\s+" + AND_OPERATOR + "\\s+(?=\\S)";
    private final String orSearchPattern = "(?<=\\S)\\s+" + OR_OPERATOR + "\\s+(?=\\S)";

    private Class entityType;
    @Getter private String query;
    @Getter private Set<String> searchTerms = new HashSet<>();
    @Getter @Setter private QueryGenerator queryGenerator;

    public Search2(Class entityType, String query) {
        this.query = query;
        this.entityType = entityType;
        this.fieldNames = stream(entityType.getDeclaredFields()).map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new));
        stream(entityType.getSuperclass().getDeclaredFields()).map(Field::getName).forEach(fieldNames::add);
        this.queryGenerator = new QueryGenerator(entityType.getSimpleName(), this.fieldNames);
        this.queryGenerator.setOR_HOLDER(OR_HOLDER);
        indexTerms();
    }

    public Search2(Class entityType){this(entityType,"");}

    private void indexTerms() {

        query = "this \"and a\" also OR \"a str\\\"ing\"";
        encode(query);
        //String unquote = query.replaceAll(QUOTE_SEARCH,);

        searchTerms = decode(encode(query));
        searchTerms = refine(searchTerms);
    }



    private String encodeSpecial(String t) {

        t = t
                .replaceAll("\\t", TAB_HOLDER)
                .replaceAll("\\s", SPACE_HOLDER);

        return trimEnds("\"", t);

    }

    private Set<String> refine (Set<String> initial) {
        return initial.stream()
                .map(t -> t = match(badKeyTerms, t).size() == 0 ?
                        stream(t.split("="))
                                .map(String::trim).collect(Collectors.joining("=")) : t)
                .map(String::trim)
                .filter(t -> !t.isEmpty() )
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String encodeTerm(String term){
        return Base64.getEncoder().encodeToString(term.getBytes());
    }

    private String decodeTerm(String encTerm){
        return new String(Base64.getDecoder().decode(encTerm));
    }

    private String encode(String qstring) {
        qstring = qstring.replaceAll("\\\\\"","***");
        for (String t : match(specialTerms, qstring)) {
            qstring = qstring.replace(t, encodeSpecial(t));

        }
        return qstring
                .replaceAll(orSearchPattern, OR_HOLDER)
                .replaceAll(" " + AND_OPERATOR + " ", " ")
                .replaceAll("\\s+", TERM_SEPARATOR);
    }

    private Set<String> decode(String toDecode) {
        return asSet(toDecode.split(TERM_SEPARATOR))
                .stream()
                .map(t -> t = t
                        .replaceAll(TAB_HOLDER, "\\t")
                        .replaceAll(SPACE_HOLDER, " "))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String getDatabaseQuery(){
        return this.queryGenerator.generateQuery(searchTerms);
    }

    private List<String> match(String pattern, String text) {
        Set<String> termList = new HashSet<>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) termList.add(m.group().trim());
        return new LinkedList<>(termList);
    }

    private String trimEnds(String rem, String target){
        if (target.startsWith(rem)) target = target.substring(1);
        if (target.endsWith(rem)) target = target.substring(0, target.length()-1);
        return target;
    }

    public Search2 setQuery(String query) {
        this.query = query;
        indexTerms();
        return this;
    }
}
