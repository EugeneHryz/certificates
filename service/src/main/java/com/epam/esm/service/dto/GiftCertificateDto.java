package com.epam.esm.service.dto;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.service.validator.DateValidity;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DateValidity(firstDate = "created", secondDate = "lastUpdated", message = "lastUpdateDate cannot be " +
        "before createDate")
public class GiftCertificateDto {

    private int id;

    @NotNull(message = "name cannot be null")
    @Size(min = 5, max = 200, message = "name must be between 5 and 200 characters in length")
    private String name;

    @Size(min = 5, max = 200, message = "duration must be between 5 and 200 characters in length")
    private String description;

    @DecimalMax(value = "10000.0", message = "price must be less or equal to 10000.0")
    @DecimalMin(value = "0.1", message = "price must be greater or equal to 0.1")
    private double price;

    @Min(value = 3, message = "duration must be greater or equal to 3")
    @Max(value = 365, message = "duration must be less or equal to 365")
    @Digits(integer = 10, fraction = 0, message = "duration must be integer value")
    private int duration;

    @NotNull(message = "created cannot be null")
    private LocalDateTime created;
    @NotNull(message = "lastUpdated cannot be null")
    private LocalDateTime lastUpdated;

    private List<TagDto> tags;

    public GiftCertificateDto() {
    }

    public GiftCertificateDto(int id, String name, String description, double price, int duration,
                              LocalDateTime created, LocalDateTime lastUpdated, List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.tags = tags;
    }

    public GiftCertificateDto(GiftCertificate certificate) {
        id = certificate.getId();
        name = certificate.getName();
        description = certificate.getDescription();
        price = certificate.getPrice();
        duration = certificate.getDuration();
        created = certificate.getCreated();
        lastUpdated = certificate.getLastUpdated();
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

    public List<TagDto> getTags() {
        return new ArrayList<>(tags);
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificateDto)) return false;
        GiftCertificateDto that = (GiftCertificateDto) o;
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
