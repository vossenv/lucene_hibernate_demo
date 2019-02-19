package com.dm.teamquery.model;


import com.dm.teamquery.config.LocalDateDeserializer;
import com.dm.teamquery.config.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "challenge")
@EqualsAndHashCode
@Getter @Setter
public class Challenge {

    @Id
    @NotNull
    @Type(type="uuid-char")
    @Column(name = "challengeid")
    private UUID challengeId;

    @NotNull
    @Column(name = "question", columnDefinition = "TEXT")
    private String question = "No question yet... ";

    @NotNull
    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer = "No answer yet... ";

    @NotNull
    @Column(name = "author")
    private String author = "anonymous";

    @NotNull
    @Column(name = "lastauthor")
    private String lastAuthor = "anonymous";

    @NotNull
    @Column(name = "enabled")
    private Boolean enabled = true;

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "datecreated")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "datelastmodified")
    private LocalDateTime dateLastModified = LocalDateTime.now();
}
