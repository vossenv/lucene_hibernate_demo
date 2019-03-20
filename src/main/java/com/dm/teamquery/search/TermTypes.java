package com.dm.teamquery.search;

public enum TermTypes {
    TEXT("TEXT"),
    QUOTED("QUOTE"),
    KEYWORD("KEYWORD"),
    AND("AND"),
    OR("OR");

    public final String name;
    TermTypes(String name){
        this.name = name;
    }
}