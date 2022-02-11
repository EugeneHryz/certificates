package com.epam.esm.repository.entity;

import com.epam.esm.repository.entity.listener.AuditEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "certificate_order")
@EntityListeners(AuditEntityListener.class)
public class Order extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "certificate_id", nullable = false)
    private GiftCertificate certificate;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal total;

    @Column(updatable = false, nullable = false, name = "purchase_date")
    private LocalDateTime purchaseDate;

    public Order() {
    }

    public Order(User user, GiftCertificate certificate, BigDecimal total, LocalDateTime date) {
        this.user = user;
        this.certificate = certificate;
        this.total = total;
        this.purchaseDate = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(GiftCertificate certificate) {
        this.certificate = certificate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
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
        return user.equals(order.user) && certificate.equals(order.certificate)
                && total.compareTo(order.total) == 0 && getId() == order.getId()
                && purchaseDate.isEqual(order.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), user, certificate, total, purchaseDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + getId() +
                "user=" + user +
                ", certificate=" + certificate +
                ", total=" + total +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
