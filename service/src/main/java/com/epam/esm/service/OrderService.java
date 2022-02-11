package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;

import javax.xml.ws.Service;
import java.util.List;

public interface OrderService {

    OrderDto placeOrder(OrderDto orderDto) throws ServiceException, NoSuchElementException;

    List<OrderDto> getUserOrders(int userId, int page, int size) throws ServiceException;

    OrderDto getUserOrder(int userId, int orderId) throws ServiceException, NoSuchElementException;

    long getUserOrderCount(int userId) throws ServiceException, NoSuchElementException;
}
