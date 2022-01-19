package com.epam.esm.service.dto;

import com.epam.esm.repository.entity.Tag;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class TagDto {

    private int id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    public TagDto() {
    }

    public TagDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagDto(Tag tag) {
        id = tag.getId();
        name = tag.getName();
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
        TagDto tagDto = (TagDto) o;
        return name.equals(tagDto.name) && id == tagDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
