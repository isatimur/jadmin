package ru.atc.jadmin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Theme entity.
 */
public class ThemeDTO implements Serializable {

    private Long id;

    private String name;

    private Long sort;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThemeDTO themeDTO = (ThemeDTO) o;

        if ( ! Objects.equals(id, themeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ThemeDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", sort='" + sort + "'" +
            '}';
    }
}
