package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SearchTerm {

    private TermTypes type;
    private String id;
    private String value;
    private Integer index;
    public static final Integer termLength = 7;

    public SearchTerm(TermTypes type, String value){
        this.type = type;
        this.value = normalize(value);
        this.id = UUID.randomUUID().toString().replaceAll("-","").substring(0, termLength);
    }

    private String normalize(String target){
        return type == TermTypes.OR || type == TermTypes.AND
                ? type.name
                : trimEndQuote(target).replaceAll("\\\\\"","\"");
    }

    private String trimEndQuote(String s){
        s = s.startsWith("\"") ? s.substring(1) : s;
        s = s.endsWith("\"") ? s.substring(0, s.length() - 1) : s;
        return s.trim();
    }
}

