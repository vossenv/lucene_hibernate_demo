package com.dm.teamquery.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "searchinfo")
public class SearchInfo {

    @Id
    @Type(type="uuid-char")
    @Column(name = "searchid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID searchId;

    @Column(name = "initialquery", columnDefinition = "VARCHAR(3000)")
    String initialQuery;

    @Column(name = "databsequery", columnDefinition = "VARCHAR(3000)")
    String databaseQuery;

    @Column(name = "errors", columnDefinition = "VARCHAR(3000)")
    String errors;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "searchdate")
    private LocalDateTime searchDate = LocalDateTime.now();

    public SearchInfo(String query, String databaseQuery){
        this.initialQuery = query;
        this.databaseQuery = databaseQuery;
    }
}
