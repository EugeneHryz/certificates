package com.epam.esm.repository.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class GiftCertificate extends AbstractEntity {

    private String name;
    private String description;
    private double price;
    private int duration;
    private LocalDateTime created;
    private LocalDateTime lastUpdated;

    public GiftCertificate() {
    }

    public GiftCertificate(String name, String description, double price, int duration,
                           LocalDateTime created, LocalDateTime lastUpdated) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return getId() == that.getId() && Double.compare(that.price, price) == 0
                && duration == that.duration && Objects.equals(name, that.name)
                && Objects.equals(description, that.description) && Objects.equals(created, that.created)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, description, price, duration, created, lastUpdated);
    }
}
