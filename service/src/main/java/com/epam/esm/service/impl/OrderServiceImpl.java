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
import com.epam.esm.service.dto.mapper.impl.OrderModelMapper;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.validator.QueryParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;
    private UserDao userDao;
    private GiftCertificateDao certificateDao;

    private OrderModelMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao,
                            GiftCertificateDao certificateDao, OrderModelMapper orderMapper) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.orderMapper = orderMapper;
    }

//    @Override
//    public OrderDto placeOrder(OrderCertificateIdOnlyDto orderCertIdDto) throws ServiceException, NoSuchElementException {
//        Optional<User> user;
//        Optional<GiftCertificate> certificate;
//        try {
//            user = userDao.findById(orderCertIdDto.getUserId());
//            certificate = certificateDao.findById(orderCertIdDto.getCertificateId());
//
//            if (!user.isPresent() || !certificate.isPresent()) {
//                throw new NoSuchElementException("User or certificate does not exist");
//            }
//        } catch (DaoException e) {
//            throw new ServiceException("Unable to place order");
//        }
//
//        LocalDateTime purchaseDate = LocalDateTime.now();
//        Order order = new Order(orderCertIdDto.getUserId(), orderCertIdDto.getCertificateId(),
//                certificate.get().getPrice(), purchaseDate);
//        try {
//            int generatedId = orderDao.create(order);
//            order.setId(generatedId);
//            return new OrderDto(order);
//        } catch (DaoException e) {
//            throw new ServiceException("Unable to create order", e);
//        }
//    }

    @Override
    public List<OrderDto> getUserOrders(int userId, String page, String size) throws ServiceException, NoSuchElementException, InvalidRequestDataException {
        QueryParamValidator validator = new QueryParamValidator();
        if (!validator.validatePositiveInteger(page) || !validator.validatePositiveInteger(size)) {
            throw new InvalidRequestDataException("Invalid pagination parameters");
        }
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);
        try {
            long count = orderDao.getUserOrderCount(userId);

            if (!validator.validatePaginationParams(pageNumber, pageSize, count)) {
                throw new InvalidRequestDataException("Invalid pagination parameters");
            }

            List<Order> userOrders = orderDao.getUserOrders(userId, pageSize, pageNumber * pageSize);
            return orderMapper.toDtoList(userOrders);
        } catch (DaoException e) {
            throw new ServiceException("Unable to get user orders", e);
        }
    }
}
