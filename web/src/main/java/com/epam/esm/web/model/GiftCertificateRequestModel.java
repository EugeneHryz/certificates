package com.epam.esm.web.model;

import com.epam.esm.service.validator.DateValidity;
import com.epam.esm.service.validator.FieldValidity;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@DateValidity(firstDate = "created", secondDate = "lastUpdated",
        message = "'created' date cannot be after 'lastUpdated' date")
public class GiftCertificateRequestModel {

    private int id;

    @FieldValidity(leftLimit = "5", rightLimit = "200",
            message = "name must be between 5 and 200 characters in length. (null if need to be unchanged)")
    private String name;

    @FieldValidity(leftLimit = "5", rightLimit = "200",
            message = "duration must be between 5 and 200 characters in length. (null if need to be unchanged)")
    private String description;

    @FieldValidity(leftLimit = "0.5", rightLimit = "10000.0",
            message = "price must be between 0.5 and 10000.0. (value 0.0 if need to be unchanged)")
    private double price;

    @FieldValidity(leftLimit = "3", rightLimit = "365",
            message = "duration must be between 3 and 365. (value 0 if need to be unchanged)")
    private int duration;

    private LocalDateTime created;
    private LocalDateTime lastUpdated;

    @Valid
    private List<TagRequestModel> tags;

    public GiftCertificateRequestModel() {
    }

    public GiftCertificateRequestModel(int id, String name, String description, double price, int duration,
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
        return id == that.id && Double.compare(that.price, price) == 0
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
