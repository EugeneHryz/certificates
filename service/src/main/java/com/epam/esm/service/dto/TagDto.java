package com.epam.esm.service.dto;

import java.util.Objects;

public class TagDto extends AbstractDto {

    private String name;

    public TagDto() {
    }

    public TagDto(String name) {
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
        TagDto tagDto = (TagDto) o;
        return name.equals(tagDto.name) && getId() == tagDto.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
