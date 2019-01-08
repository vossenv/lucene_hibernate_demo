package com.dm.teamquery.model;


import com.dm.teamquery.config.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "challengeid")
    private UUID challengeId;

    @Column(name = "question", columnDefinition = "VARCHAR(3000)")
    private String question;

    @Column(name = "answer", columnDefinition = "VARCHAR(3000)")
    private String answer;

    @Column(name = "author")
    private String author;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "datecreated")
    private LocalDateTime dateCreated;

    @Column(name = "lastauthor")
    private String lastAuthor;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "datelastmodified")
    private LocalDateTime dateLastModified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Challenge challenge = (Challenge) o;

        if (challengeId != null ? !challengeId.equals(challenge.challengeId) : challenge.challengeId != null)
            return false;
        if (question != null ? !question.equals(challenge.question) : challenge.question != null) return false;
        if (answer != null ? !answer.equals(challenge.answer) : challenge.answer != null) return false;
        if (author != null ? !author.equals(challenge.author) : challenge.author != null) return false;
        if (dateCreated != null ? !dateCreated.equals(challenge.dateCreated) : challenge.dateCreated != null)
            return false;
        if (lastAuthor != null ? !lastAuthor.equals(challenge.lastAuthor) : challenge.lastAuthor != null) return false;
        return dateLastModified != null ? dateLastModified.equals(challenge.dateLastModified) : challenge.dateLastModified == null;
    }

    @Override
    public int hashCode() {
        int result = challengeId != null ? challengeId.hashCode() : 0;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (lastAuthor != null ? lastAuthor.hashCode() : 0);
        result = 31 * result + (dateLastModified != null ? dateLastModified.hashCode() : 0);
        return result;
    }

    public UUID getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(UUID challengeId) {
        this.challengeId = challengeId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastAuthor() {
        return lastAuthor;
    }


    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }

    public LocalDateTime getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(LocalDateTime dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

//    public void setDateLastModified(LocalDate dateLastModified) {
//        this.dateLastModified = dateLastModified.atStartOfDay();
//    }
//    public void setDateCreated(LocalDate dateCreated) {
//        this.dateCreated = dateCreated.atStartOfDay();
//    }


}
