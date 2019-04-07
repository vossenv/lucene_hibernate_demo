package com.dm.teamquery.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "searchinfo")
public class SearchInfo extends EntityBase<String> {

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

    @Column(name = "errors", columnDefinition = "VARCHAR(3000)")
    String errors;

    public SearchInfo(String query, String errors){
        this.initialQuery = query;
        this.errors = errors;
    }
}
