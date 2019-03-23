package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SearchTerm {

    private String id;
    private String value;
    private TermTypes type;
    private Integer index = -1;
    public static final Integer termLength = 10;

    public SearchTerm(TermTypes type, String value) {
        this.type = type;
        this.value = normalize(value);
        this.id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, termLength);
    }

    private String normalize(String s) {
        switch (type){
            case OR:
            case AND: return type.name;
            case QUOTED: return trimQuotes(s);
            default: return s.trim();
        }
    }

    private String trimQuotes(String s) {
        s = s.startsWith("\"") ? s.substring(1) : s;
        s = s.endsWith("\"") ? s.substring(0, s.length() - 1) : s;
        s = s.trim().isEmpty() ? s : s.trim();
        return s.replaceAll("\\\\\"", "\"");
    }
}

