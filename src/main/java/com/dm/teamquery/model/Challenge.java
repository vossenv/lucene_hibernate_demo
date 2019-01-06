package com.dm.teamquery.model;


import javax.persistence.*;

@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challengeid")
    private int challengeId;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "author")
    private String author;

    @Column(name = "datelastmodified")
    private String dateLastModified;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
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

    public String getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(String dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Challenge challenge = (Challenge) o;

        if (challengeId != challenge.challengeId) return false;
        if (question != null ? !question.equals(challenge.question) : challenge.question != null) return false;
        if (answer != null ? !answer.equals(challenge.answer) : challenge.answer != null) return false;
        if (author != null ? !author.equals(challenge.author) : challenge.author != null) return false;
        return dateLastModified != null ? dateLastModified.equals(challenge.dateLastModified) : challenge.dateLastModified == null;
    }

    @Override
    public int hashCode() {
        int result = challengeId;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (dateLastModified != null ? dateLastModified.hashCode() : 0);
        return result;
    }
}
