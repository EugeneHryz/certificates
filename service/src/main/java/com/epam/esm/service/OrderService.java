package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;

import java.util.List;

public interface OrderService {

//    OrderDto placeOrder(OrderCertificateIdOnlyDto orderCertIdDto) throws ServiceException, NoSuchElementException;

    List<OrderDto> getUserOrders(int userId, String page, String size) throws ServiceException, NoSuchElementException, InvalidRequestDataException;
}
