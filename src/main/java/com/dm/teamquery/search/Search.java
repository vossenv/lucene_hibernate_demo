package com.dm.teamquery.search;


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
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "search")
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
public class Search {

    @Transient private Pageable page;
    @Transient private String currentQuery;
    @Transient private Set<String> andTerms = new HashSet<>();
    @Transient private Set<String> orTerms = new HashSet<>();
    @Transient private Set<String> toProcess = new HashSet<>();

    @Id
    @Type(type="uuid-char")
    @Column(name = "searchid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID searchId;

    @Column(name = "initialquery")
    String initiaQuery;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "searchdate")
    private LocalDateTime searchDate;

    public Search(String query, Pageable page){
        this.initiaQuery = query;
        this.currentQuery = query;
        this.page = page;
    }

    public Search(String query){
        this.initiaQuery = query;
        this.currentQuery = query;
    }

}
