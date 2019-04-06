package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.queryparser.classic.ParseException;

import static com.dm.teamquery.search.SearchTerm.Types.*;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@Setter
public class SearchTerm {

    private String id;
    private String value;
    private String key;
    private Types type;
    public static final Integer idLength = 10;

    public SearchTerm(Types type, String value) throws ParseException{
        this.type = type;
        this.value = normalize(value);
        if (type.is(KEYWORD) && this.value.trim().isEmpty()) throw new ParseException("Empty value for keyword: " + key);
        if (type.is(KEYWORD) && this.key.trim().isEmpty()) throw new ParseException("Empty keyword not allowed for value: " + value);
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
        s = s.replaceFirst("\\s*" + Pattern.quote(key) + "\\s*:", "").trim();
        return s.replace(Pattern.quote(key),key);
    }

    public String getValue() {
        return type.is(KEYWORD) ? key + ":" + value : value;
    }

    public enum Types {
        TEXT, QUOTED, KEYWORD, AND, OR, NOT, BOOLEAN;
        public boolean is(Types type){
            return type != BOOLEAN ? this == type : (this == AND || this == OR || this == NOT);}

        public static boolean isBoolean (String s) {
            return s.equals(AND.toString()) || s.equals(OR.toString()) || s.equals(NOT.toString());
        }
    }
}

