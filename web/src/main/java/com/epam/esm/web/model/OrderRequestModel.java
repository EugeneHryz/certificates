package com.epam.esm.web.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderRequestModel {

    private int id;
    private int userId;
    private int certificateId;
    private BigDecimal total;
    private LocalDateTime purchaseDate;

    public OrderRequestModel() {
    }

    public OrderRequestModel(int id, int userId, int certificateId, BigDecimal total, LocalDateTime date) {
        this.id = id;
        this.userId = userId;
        this.certificateId = certificateId;
        this.total = total;
        this.purchaseDate = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (!(o instanceof OrderRequestModel)) return false;
        OrderRequestModel that = (OrderRequestModel) o;
        return id == that.id && userId == that.userId && certificateId == that.certificateId
                && total.compareTo(that.total) == 0 && Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, certificateId, total, purchaseDate);
    }
}
