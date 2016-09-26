package ru.atc.jadmin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Faq.
 */
@Entity
@Table(name = "faq")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "faq")
public class Faq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "sort")
    private Long sort;

    @ManyToOne
    private Theme theme;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Faq name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestionText() {
        return questionText;
    }

    public Faq questionText(String questionText) {
        this.questionText = questionText;
        return this;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Faq answerText(String answerText) {
        this.answerText = answerText;
        return this;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Long getSort() {
        return sort;
    }

    public Faq sort(Long sort) {
        this.sort = sort;
        return this;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Theme getTheme() {
        return theme;
    }

    public Faq theme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Faq faq = (Faq) o;
        if(faq.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, faq.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Faq{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", questionText='" + questionText + "'" +
            ", answerText='" + answerText + "'" +
            ", sort='" + sort + "'" +
            '}';
    }
}
