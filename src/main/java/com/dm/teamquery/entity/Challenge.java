package com.dm.teamquery.entity;


import com.dm.teamquery.search.UUIDFieldBridge;
import lombok.Data;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@Entity
@Indexed
@Table(name = "challenge")

public class Challenge extends EntityBase<String> {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "challengeid")
    @FieldBridge(impl = UUIDFieldBridge.class)
    @Analyzer(impl = KeywordAnalyzer.class)
    private UUID challengeId;

    @Field
    @NotNull(message = "Question cannot be blank")
    @Size(min = 3, message = "Question must be more than 6 characters")
    @Column(name = "question", columnDefinition = "TEXT")
    private String question;

    @Field
    @NotNull(message = "Answer cannot be blank")
    @Size(min = 3, message = "Answer must be more than 6 characters")
    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Field
    @NotNull
    @Column(name = "author", updatable = false)
    private String author;

    @Field
    @NotNull
    @Column(name = "lastauthor")
    private String lastAuthor = "anonymous";


}
