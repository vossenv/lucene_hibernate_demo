package com.dm.teamquery.entity;



import com.dm.teamquery.search.custom_bridge.UUIDFieldBridge;
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
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@Entity
@Indexed
@Table(name = "movie")

public class Movie extends EntityBase<String> {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "movieid")
    @FieldBridge(impl = UUIDFieldBridge.class)
    @Analyzer(impl = KeywordAnalyzer.class)
    private UUID movieId;

    @Field
    @NotNull(message = "Title cannot be blank")
    @Size(min = 1, message = "Question must be more than 1 characters")
    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Field
    @NotNull(message = "Description cannot be blank")
    @Size(min = 3, message = "Description must be more than 3 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Field
    @Column(name = "url", columnDefinition = "VARCHAR(500)")
    private String url;

    @Field
    @Column(name = "cover", columnDefinition = "VARCHAR(500)")
    private String cover;

    @Field
    @Column(name = "catchphrase", columnDefinition = "VARCHAR(500)")
    private String catchPhrase;

    @Field
    @Column(name = "producer", columnDefinition = "VARCHAR(500)")
    private String producer;

    @Field
    @Column(name = "genre", columnDefinition = "VARCHAR(500)")
    private String genre;

    @Field
    @Column(name = "country", columnDefinition = "VARCHAR(500)")
    private String country;

    @Field
    @Column(name = "avatar", columnDefinition = "VARCHAR(500)")
    private String avatar;

    @Field
    @Column(name = "rating", columnDefinition = "TINYINT(2)")
    private String rating;


}
