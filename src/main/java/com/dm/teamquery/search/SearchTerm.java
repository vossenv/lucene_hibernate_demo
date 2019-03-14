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

    public SearchTerm(Types type, String value){
        this.type = type;
        this.value = value;
        this.id = UUID.randomUUID().toString().replaceAll("-","");
    }

    public enum Types{ TEXT, QUOTED, KEYWORD }
}

