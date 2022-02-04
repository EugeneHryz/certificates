package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class OrderDtoConverter extends AbstractTwoWayConverter<OrderDto, Order> {

    @Override
    protected Order convertTo(OrderDto source) {
        Order order = new Order();
        try {
            BeanUtils.copyProperties(order, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    protected OrderDto convertBack(Order source) {
        OrderDto orderDto = new OrderDto();
        try {
            BeanUtils.copyProperties(orderDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return orderDto;
    }
}
