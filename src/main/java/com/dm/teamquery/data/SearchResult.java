package com.dm.teamquery.data;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Data
public class SearchResult {

    private long rowCount;
    private BigDecimal searchTime;
    private BigDecimal totalTime;
    private Collection resultsList;
    private String originalQuery;

    public List<?> getResultsList() {
        return new LinkedList<>(resultsList);
    }

    public void setResultsList(Collection resultsList) {
        this.resultsList = new HashSet<>(resultsList);
    }

    public void setSearchTime(Long searchTime) {
        this.searchTime = new BigDecimal(searchTime.toString()).movePointLeft(9);
    }
    public void setTotalTime(Long totalTime) {
        this.searchTime = new BigDecimal(totalTime.toString()).movePointLeft(9);
    }

    public Double getSearchTime() {
        return searchTime.doubleValue();
    }

    public Double getTotalTime() {
        return totalTime.doubleValue();
    }
}
