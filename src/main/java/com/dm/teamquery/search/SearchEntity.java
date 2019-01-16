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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "searchentity")
@Getter @Setter @EqualsAndHashCode @NoArgsConstructor
public class SearchEntity {

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
    private LocalDateTime searchDate = LocalDateTime.now();

    public SearchEntity(String query){
        this.initiaQuery = query;
    }
}
