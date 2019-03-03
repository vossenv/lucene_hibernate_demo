package com.dm.teamquery.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@Entity
@Table(name = "challenge")
public class Challenge extends Auditable<String>  {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
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
    private String author;

    @NotNull
    @Column(name = "lastauthor")
    private String lastAuthor = "anonymous";


}
