package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.mapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderModelMapper implements ModelMapper<Order, OrderDto> {

    @Override
    public Order toEntity(OrderDto dto) {
        Order order = new Order(dto.getUserId(), dto.getCertificateId(), dto.getTotal(), dto.getDate());
        order.setId(dto.getId());
        return order;
    }

    @Override
    public OrderDto toDto(Order entity) {
        OrderDto orderDto = new OrderDto(entity.getUserId(), entity.getCertificateId(),
                entity.getTotal(), entity.getDate());
        orderDto.setId(entity.getId());
        return orderDto;
    }
}
