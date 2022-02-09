package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

public class OrderDtoConverter extends AbstractTwoWayConverter<OrderDto, Order> {

    private final Logger logger = LoggerFactory.getLogger(OrderDtoConverter.class);

    @Override
    protected Order convertTo(OrderDto source) {
        Order order = new Order();
        try {
            BeanUtils.copyProperties(order, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from OrderDto to Order", e);
        }
        return order;
    }

    @Override
    protected OrderDto convertBack(Order source) {
        OrderDto orderDto = new OrderDto();
        try {
            BeanUtils.copyProperties(orderDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from Order to OrderDto", e);
        }
        return orderDto;
    }
}
