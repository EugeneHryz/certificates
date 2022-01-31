package com.epam.esm.web.model.mapper.impl;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.model.OrderRequestModel;
import com.epam.esm.web.model.mapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderModelMapper implements ModelMapper<OrderDto, OrderRequestModel> {

    @Override
    public OrderDto toDto(OrderRequestModel requestModel) {
        OrderDto orderDto = new OrderDto(requestModel.getUserId(), requestModel.getCertificateId(),
                requestModel.getTotal(), requestModel.getPurchaseDate());
        orderDto.setId(requestModel.getId());
        return orderDto;
    }

    @Override
    public OrderRequestModel toRequestModel(OrderDto dto) {
        OrderRequestModel orderRequestModel = new OrderRequestModel(dto.getId(), dto.getUserId(),
                dto.getCertificateId(), dto.getTotal(), dto.getPurchaseDate());
        return orderRequestModel;
    }
}
