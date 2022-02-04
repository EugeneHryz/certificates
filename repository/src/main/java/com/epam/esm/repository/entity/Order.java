package com.epam.esm.repository.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order extends AbstractEntity {

    private int userId;
    private int certificateId;
    private double total;
    private LocalDateTime purchaseDate;

    public Order() {
    }

    public Order(int userId, int certificateId, double total, LocalDateTime purchaseDate) {
        this.userId = userId;
        this.certificateId = certificateId;
        this.total = total;
        this.purchaseDate = purchaseDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        return userId == order.userId && certificateId == order.certificateId
                && Double.compare(order.total, total) == 0 && getId() == order.getId()
                && purchaseDate.isEqual(order.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), userId, certificateId, total, purchaseDate);
    }
}
