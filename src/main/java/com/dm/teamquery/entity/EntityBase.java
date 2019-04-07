package com.dm.teamquery.entity;


import com.dm.teamquery.search.LocalDateFieldBridge;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.shingle.ShingleFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.ClassicFilterFactory;
import org.apache.lucene.analysis.standard.ClassicTokenizerFactory;
import org.apache.lucene.analysis.standard.UAX29URLEmailTokenizerFactory;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.hibernate.search.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

@AnalyzerDef(name = "customanalyzer",
        tokenizer = @TokenizerDef(factory = ClassicTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
                @TokenFilterDef(factory = EnglishPossessiveFilterFactory.class),
                @TokenFilterDef(factory = SynonymFilterFactory.class, params = {
                        @Parameter(name = "synonyms", value = "synonyms.txt"),
                        @Parameter(name = "ignoreCase", value = "true")
                })

        })
@Analyzer(definition = "customanalyzer")
public class EntityBase<U> {

    @CreatedDate
    @Field(analyze = Analyze.NO)
    @FieldBridge(impl = LocalDateFieldBridge.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_modified_date")
    @Field(analyze = Analyze.NO)
    @FieldBridge(impl = LocalDateFieldBridge.class)
    private LocalDateTime lastModifiedDate;

    @Field
    @NotNull
    @Column(name = "enabled")
    private Boolean enabled = true;

}