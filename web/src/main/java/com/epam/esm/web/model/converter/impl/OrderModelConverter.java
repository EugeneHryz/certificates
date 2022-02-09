package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.model.OrderRequestModel;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

public class OrderModelConverter extends AbstractTwoWayConverter<OrderRequestModel, OrderDto> {

    private final Logger logger = LoggerFactory.getLogger(OrderModelConverter.class);

    @Override
    protected OrderDto convertTo(OrderRequestModel source) {
        OrderDto orderDto = new OrderDto();
        try {
            BeanUtils.copyProperties(orderDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from OrderRequestModel to OrderDto", e);
        }
        return orderDto;
    }

    @Override
    protected OrderRequestModel convertBack(OrderDto source) {
        OrderRequestModel orderModel = new OrderRequestModel();
        try {
            BeanUtils.copyProperties(orderModel, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from OrderDto to OrderRequestModel", e);
        }
        return orderModel;
    }
}
