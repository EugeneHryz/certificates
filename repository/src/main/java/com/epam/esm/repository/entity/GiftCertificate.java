package com.epam.esm.repository.entity;

import com.epam.esm.repository.entity.listener.AuditEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gift_certificate")
@EntityListeners(AuditEntityListener.class)
//@NamedNativeQuery(name = "findCertificates",
//        query = "SELECT *\n" +
//                "FROM gift_certificate\n" +
//                "         INNER JOIN (SELECT certificate_id\n" +
//                "                     FROM certificate_tag_mapping\n" +
//                "                     WHERE tag_id IN (SELECT id FROM tag WHERE name = 'fun' OR name = 'amusement park')\n" +
//                "                     GROUP BY certificate_id\n" +
//                "                     HAVING COUNT(*) = 2) mc\n" +
//                "                    ON mc.certificate_id = id\n" +
//                "WHERE (name LIKE '%%' OR description LIKE '%%')\n" +
//                "ORDER BY last_update_date DESC;")
public class GiftCertificate extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false, name = "last_updated")
    private LocalDateTime lastUpdated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "certificate_tag_mapping",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id",
            unique = true)
    )
    private List<Tag> tags;

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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + getId() +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", created=" + created +
                ", lastUpdated=" + lastUpdated +
                ", tags=" + tags +
                '}';
    }
}
