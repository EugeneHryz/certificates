package com.epam.esm.web.model.mapper.impl;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.model.OrderRequestModel;
import com.epam.esm.web.model.mapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderModelMapper implements ModelMapper<OrderDto, OrderRequestModel> {

    @Override
    public OrderDto toDto(OrderRequestModel requestModel) {
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(requestModel.getUserId());
        orderDto.setCertificateId(requestModel.getCertificateId());
        return orderDto;
    }

    @Override
    public OrderRequestModel toRequestModel(OrderDto dto) {
        OrderRequestModel orderRequestModel = new OrderRequestModel(dto.getUserId(), dto.getCertificateId());
        return orderRequestModel;
    }
}