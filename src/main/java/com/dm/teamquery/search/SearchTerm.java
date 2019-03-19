package com.dm.teamquery.search;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SearchTerm {

    private Types type;
    private String id;
    private String value;
    private Integer index;
    public static final Integer termLength = 7;

    public SearchTerm(Types type, String value){
        this.type = type;
        this.value = normalize(value);
        this.id = UUID.randomUUID().toString().replaceAll("-","").substring(0, termLength);
    }

    public String getQuotedValue(){
        return type == Types.QUOTED ? "\"" + getValue() + "\"" : getValue();
    }

    private String normalize(String target){
        if (type == Types.OR || type == Types.AND) return type.toString();
        target = target.startsWith("\"") ? target.substring(1) : target;
        target =  target.endsWith("\"") ? target.substring(0, target.length()-1) : target;
        return target.trim();
    }

    public enum Types{ TEXT, QUOTED, KEYWORD, AND, OR }
}

