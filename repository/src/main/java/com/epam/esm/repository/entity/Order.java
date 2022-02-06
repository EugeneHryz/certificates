package com.epam.esm.repository.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "certificate_order")
public class Order extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    private int certificateId;

    @Column(nullable = false)
    private double total;

    @Column(updatable = false, nullable = false)
    private LocalDateTime purchaseDate;

    public Order() {
    }

    public Order(User user, int certificateId, double total, LocalDateTime date) {
        this.user = user;
        this.certificateId = certificateId;
        this.total = total;
        this.purchaseDate = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return user.equals(order.user) && certificateId == order.certificateId
                && Double.compare(order.total, total) == 0 && getId() == order.getId()
                && purchaseDate.isEqual(order.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), user, certificateId, total, purchaseDate);
    }
}
