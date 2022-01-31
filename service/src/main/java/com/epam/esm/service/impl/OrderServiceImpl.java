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
import com.epam.esm.service.dto.mapper.impl.OrderDtoMapper;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.validator.QueryParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceImpl implements OrderService {

    public static final int ORDER_CODE = 4;

    private OrderDao orderDao;
    private UserDao userDao;
    private GiftCertificateDao certificateDao;
    private OrderDtoMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao,
                            GiftCertificateDao certificateDao, OrderDtoMapper orderMapper) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.orderMapper = orderMapper;
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
        } catch (DaoException e) {
            throw new ServiceException("Unable to place order", ORDER_CODE);
        }

        LocalDateTime purchaseDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Order order = new Order(orderDto.getUserId(), orderDto.getCertificateId(),
                certificate.get().getPrice(), purchaseDate);
        try {
            int generatedId = orderDao.create(order);
            order.setId(generatedId);
            return orderMapper.toDto(order);
        } catch (DaoException e) {
            throw new ServiceException("Unable to create order", e, ORDER_CODE);
        }
    }

    @Override
    public List<OrderDto> getUserOrders(int userId, String page, String size) throws ServiceException, NoSuchElementException, InvalidRequestDataException {
        QueryParamValidator validator = new QueryParamValidator();
        if (!validator.validatePositiveInteger(page) || !validator.validatePositiveInteger(size)) {
            throw new InvalidRequestDataException("Invalid pagination parameters", ORDER_CODE);
        }
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);
        try {
            List<Order> userOrders = orderDao.getUserOrders(userId, pageSize, pageNumber * pageSize);
            return orderMapper.toDtoList(userOrders);

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
            return orderMapper.toDto(order.get());

        } catch (DaoException e) {
            throw new ServiceException("Unable to find order (orderId = " + orderId + ")", e, ORDER_CODE);
        }
    }
}
