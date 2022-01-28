package com.epam.esm.web.model;

import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.UserDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserRequestModel {

    private int id;

    @NotNull(message = "user name cannot be null")
    @Size(min = 3, max = 50, message = "user name must be between 3 and 50 characters in length")
    private String name;

    public UserRequestModel() {
    }

    public UserRequestModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserRequestModel(User user) {
        id = user.getId();
        name = user.getName();
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
        if (!(o instanceof UserRequestModel)) return false;
        UserRequestModel user = (UserRequestModel) o;
        return id == user.id && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
