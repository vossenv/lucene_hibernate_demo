package com.dm.teamquery.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "challenge")
public class Challenge extends ResourceSupport {

    @Id
    @Type(type="uuid-char")
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
    @Column(name = "challengeid")
    private UUID challengeId;

    @NotNull(message = "Question cannot be blank")
    @Size(min = 3, message = "Question must be more than 6 characters")
    @Column(name = "question", columnDefinition = "TEXT")
    private String question;

    @NotNull(message = "Answer cannot be blank")
    @Size(min = 3, message = "Answer must be more than 6 characters")
    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @NotNull
    @Column(name = "author", updatable = false)
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
    @Column(name = "datecreated", updatable = false)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "datelastmodified")
    private LocalDateTime dateLastModified = LocalDateTime.now();


}
