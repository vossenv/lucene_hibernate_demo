package com.dm.teamquery.entity;


import com.dm.teamquery.search.UUIDFieldBridge;
import lombok.Data;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

//@AnalyzerDef(name = "ngram",
//        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class ),
//        filters = {
//                @TokenFilterDef(factory = StandardFilterFactory.class),
//                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                @TokenFilterDef(factory = StopFilterFactory.class),
//                @TokenFilterDef(factory = NGramFilterFactory.class,
//                        params = {
//                                @Parameter(name = "minGramSize", value = "3"),
//                                @Parameter(name = "maxGramSize", value = "3") } )
//        }
//)

@Data
@Entity
@Indexed
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
    @FieldBridge(impl = UUIDFieldBridge.class)
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
