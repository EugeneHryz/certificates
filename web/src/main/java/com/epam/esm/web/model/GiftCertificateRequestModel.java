package com.epam.esm.web.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GiftCertificateRequestModel {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    private LocalDateTime created;
    private LocalDateTime lastUpdated;

    private List<TagRequestModel> tags;

    public GiftCertificateRequestModel() {
    }

    public GiftCertificateRequestModel(int id, String name, String description, BigDecimal price, int duration,
                                       LocalDateTime created, LocalDateTime lastUpdated, List<TagRequestModel> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.tags = tags;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    public List<TagRequestModel> getTags() {
        return tags;
    }

    public void setTags(List<TagRequestModel> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificateRequestModel)) return false;
        GiftCertificateRequestModel that = (GiftCertificateRequestModel) o;
        return id == that.id && price.equals(that.price)
                && duration == that.duration && Objects.equals(name, that.name)
                && Objects.equals(description, that.description) && Objects.equals(created, that.created)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && tags.equals(that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, created, lastUpdated, tags);
    }
}
