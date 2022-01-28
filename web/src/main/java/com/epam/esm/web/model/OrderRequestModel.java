package com.epam.esm.web.model;

import java.util.Objects;

public class OrderRequestModel {

    private int userId;
    private int certificateId;

    public OrderRequestModel() {
    }

    public OrderRequestModel(int userId, int certificateId) {
        this.userId = userId;
        this.certificateId = certificateId;
    }

    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderRequestModel)) return false;
        OrderRequestModel that = (OrderRequestModel) o;
        return userId == that.userId && certificateId == that.certificateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, certificateId);
    }
}
