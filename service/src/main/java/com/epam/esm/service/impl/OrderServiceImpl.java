package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {

    public static final int ORDER_CODE = 4;

    private OrderDao orderDao;
    private UserDao userDao;
    private GiftCertificateDao certificateDao;

    private ConversionService conversionService;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, GiftCertificateDao certificateDao,
                            ConversionService conversionService) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.conversionService = conversionService;
    }

    @Override
    public OrderDto placeOrder(OrderDto orderDto) throws ServiceException, NoSuchElementException {
        Optional<User> user;
        Optional<GiftCertificate> certificate;
        try {
            user = userDao.findById(orderDto.getUserId());
            certificate = certificateDao.findById(orderDto.getCertificateId());

            if (!user.isPresent() || !certificate.isPresent()) {
                throw new NoSuchElementException("User or certificate does not exist", ORDER_CODE);
            }
            LocalDateTime purchaseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Order order = conversionService.convert(orderDto, Order.class);
            order.setPurchaseDate(purchaseDate);

            int generatedId = orderDao.create(order);
            order.setId(generatedId);
            return conversionService.convert(order, OrderDto.class);

        } catch (DaoException e) {
            throw new ServiceException("Unable to place order", e, ORDER_CODE);
        }
    }

    @Override
    public List<OrderDto> getUserOrders(int userId, int pageNumber, int pageSize) throws ServiceException {
        try {
            List<Order> userOrders = orderDao.getUserOrders(userId, pageSize, pageNumber * pageSize);
            return userOrders.stream().map(u -> conversionService.convert(u, OrderDto.class))
                    .collect(Collectors.toList());

        } catch (DaoException e) {
            throw new ServiceException("Unable to get user orders", e, ORDER_CODE);
        }
    }

    @Override
    public OrderDto getUserOrder(int userId, int orderId) throws ServiceException, NoSuchElementException {
        try {
            Optional<Order> order = orderDao.findById(orderId);
            if (!order.isPresent() || order.get().getUserId() != userId) {
                throw new NoSuchElementException("Cannot find order (orderId = " + orderId +
                        ") for a user (userId = " + userId + ")", ORDER_CODE);
            }
            return conversionService.convert(order, OrderDto.class);

        } catch (DaoException e) {
            throw new ServiceException("Unable to find order (orderId = " + orderId + ")", e, ORDER_CODE);
        }
    }

    @Override
    public long getUserOrderCount(int userId) throws ServiceException, NoSuchElementException {
        try {
            Optional<User> user = userDao.findById(userId);
            if (!user.isPresent()) {
                throw new NoSuchElementException("Unable to find user (useId = " + userId + ")", ORDER_CODE);
            }
            return orderDao.getUserOrderCount(userId);

        } catch (DaoException e) {
            throw new ServiceException("Unable to count all user orders", e, ORDER_CODE);
        }
    }
}
