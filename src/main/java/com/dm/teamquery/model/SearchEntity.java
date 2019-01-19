package com.dm.teamquery.model;


import com.dm.teamquery.config.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "searchentity")
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
public class SearchEntity {

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

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "searchdate")
    private LocalDateTime searchDate = LocalDateTime.now();

    public SearchEntity(String query, String databaseQuery){
        this.initialQuery = query;
        this.databaseQuery = databaseQuery;
    }
}
