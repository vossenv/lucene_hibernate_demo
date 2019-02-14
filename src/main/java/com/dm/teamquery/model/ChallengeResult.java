package com.dm.teamquery.model;

import java.util.*;

public class ChallengeResult extends SearchResult{

    @Override
    public List<Challenge> getResultsList() {
        return (LinkedList<Challenge>)super.getResultsList();
    }
}
