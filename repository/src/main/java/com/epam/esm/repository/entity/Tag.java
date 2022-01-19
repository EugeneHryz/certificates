package com.epam.esm.repository.entity;

import java.util.Objects;

public class Tag extends AbstractEntity {

    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name) && getId() == tag.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
