package com.dm.teamquery.entity;


import com.dm.teamquery.search.custom_bridge.LocalDateFieldBridge;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.standard.ClassicTokenizerFactory;
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

@AnalyzerDef(
        name = "customanalyzer",
        tokenizer = @TokenizerDef(factory = ClassicTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
                @TokenFilterDef(factory = EnglishPossessiveFilterFactory.class),
                @TokenFilterDef(factory = SynonymFilterFactory.class, params = {
                        @Parameter(name = "synonyms", value = "synonyms.txt"),
                        @Parameter(name = "ignoreCase", value = "true")})
        }
)

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Analyzer(definition = "customanalyzer")
public class EntityBase<U> {

    @CreatedDate
    @Field(analyze = Analyze.NO)
    @FieldBridge(impl = LocalDateFieldBridge.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_modified_date")
    @Field(analyze = Analyze.NO)
    @FieldBridge(impl = LocalDateFieldBridge.class)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Field
    @NotNull
    @Column(name = "enabled")
    private Boolean enabled = true;

}