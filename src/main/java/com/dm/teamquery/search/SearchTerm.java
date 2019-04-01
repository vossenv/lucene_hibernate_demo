package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;

import static com.dm.teamquery.search.SearchTerm.Types.*;
import java.util.UUID;

@Getter
@Setter
public class SearchTerm {

    private String id;
    private String value;
    private String key;
    private Types type;
    public static final Integer idLength = 10;

    public SearchTerm(Types type, String value) {
        this.type = type;
        this.value = normalize(value);
        this.id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, idLength);
    }

    private String normalize(String s) {
        return (type.is(KEYWORD)) ? formatKeyword(s) : s.trim();
    }

    private String formatKeyword(String s) {
        String k = s.split(":")[0].trim();
        if (k.isEmpty() || s.replace(":","").trim().isEmpty()) {
            this.type = Types.TEXT;
            return s.trim();
        }
        key = k;
        return s.replaceFirst("\\s*" + key + "\\s*:", "").trim();
    }

    public String getValue() {
        return type.is(KEYWORD) ? key + ":" + value : value;
    }

    public enum Types {
        TEXT, QUOTED, KEYWORD, AND, OR, BOOLEAN;
        public boolean is(Types type){
            return type != BOOLEAN ? this == type : (this == AND || this == OR);}
    }
}

