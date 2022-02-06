package com.epam.esm.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class User extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    public User() {
    }

    public User(String name) {
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
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return name.equals(user.name) && getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
