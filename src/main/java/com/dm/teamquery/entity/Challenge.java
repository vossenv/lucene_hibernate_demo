package com.dm.teamquery.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "challenge")
public class Challenge extends ResourceSupport {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "datecreated")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "datelastmodified")
    private LocalDateTime dateLastModified = LocalDateTime.now();
}
