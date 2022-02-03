package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.model.OrderRequestModel;
import com.epam.esm.web.model.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class OrderModelConverter extends AbstractTwoWayConverter<OrderRequestModel, OrderDto> {

    @Override
    protected OrderDto convertTo(OrderRequestModel source) {
        OrderDto orderDto = new OrderDto();
        try {
            BeanUtils.copyProperties(orderDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // ignore :(
            e.printStackTrace();
        }
        return orderDto;
    }

    @Override
    protected OrderRequestModel convertBack(OrderDto source) {
        OrderRequestModel orderModel = new OrderRequestModel();
        try {
            BeanUtils.copyProperties(orderModel, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return orderModel;
    }
}
