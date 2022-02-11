package com.epam.esm.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDto extends AbstractDto {

    private int userId;
    private int certificateId;
    private BigDecimal total;
    private LocalDateTime purchaseDate;

    public OrderDto() {
    }

    public OrderDto(int userId, int certificateId, BigDecimal total, LocalDateTime date) {
        this.userId = userId;
        this.certificateId = certificateId;
        this.total = total;
        this.purchaseDate = date;
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
        if (!(o instanceof OrderDto)) return false;
        OrderDto orderDto = (OrderDto) o;
        return getId() == orderDto.getId() && userId == orderDto.userId && certificateId == orderDto.certificateId
                && total.compareTo(orderDto.total) == 0 && Objects.equals(purchaseDate, orderDto.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), userId, certificateId, total, purchaseDate);
    }
}
