package ru.atc.jadmin.service.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import ru.atc.jadmin.domain.Faq;

/**
 * Created by developer on 9/24/16.
 */
public class ThemeAndFaqDTO {

    private Long id;

    private String name;

    private Long sort;

    private Set<Faq> faqIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Set<Faq> getFaqIds() {
        return faqIds;
    }

    public void setFaqIds(Set<Faq> faqIds) {
        this.faqIds = faqIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThemeAndFaqDTO themeDTO = (ThemeAndFaqDTO)o;

        if (!Objects.equals(id, themeDTO.id))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override public String toString() {
        return "ThemeAndFaqDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", sort=" + sort +
            ", faqIds=" + faqIds +
            '}';
    }
}
