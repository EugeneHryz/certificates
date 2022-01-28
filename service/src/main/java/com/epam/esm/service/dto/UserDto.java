package com.epam.esm.service.dto;

import java.util.Objects;

public class UserDto extends AbstractDto {

    private String name;

    public UserDto() {
    }

    public UserDto(String name) {
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
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return getId() == userDto.getId() && Objects.equals(name, userDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
