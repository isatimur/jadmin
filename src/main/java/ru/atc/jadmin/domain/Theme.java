package ru.atc.jadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Theme.
 */
@Entity
@Table(name = "theme")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "theme")
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sort")
    private Long sort;

    @OneToMany(mappedBy = "theme")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Faq> faqs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Theme name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSort() {
        return sort;
    }

    public Theme sort(Long sort) {
        this.sort = sort;
        return this;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Set<Faq> getFaqs() {
        return faqs;
    }

    public Theme faqs(Set<Faq> faqs) {
        this.faqs = faqs;
        return this;
    }

    public Theme addFaq(Faq faq) {
        faqs.add(faq);
        faq.setTheme(this);
        return this;
    }

    public Theme removeFaq(Faq faq) {
        faqs.remove(faq);
        faq.setTheme(null);
        return this;
    }

    public void setFaqs(Set<Faq> faqs) {
        this.faqs = faqs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        if(theme.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Theme{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", sort='" + sort + "'" +
            '}';
    }
}
