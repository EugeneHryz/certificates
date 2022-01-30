package com.epam.esm.web.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class TagRequestModel {

    private int id;

    @NotNull(message = "name cannot be null")
    @Size(min = 3, max = 100, message = "name must be between 3 and 100 characters long")
    private String name;

    public TagRequestModel() {
    }

    public TagRequestModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        TagRequestModel tag = (TagRequestModel) o;
        return name.equals(tag.name) && id == tag.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
