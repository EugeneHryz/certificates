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
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private ConversionService conversionService;

    @Mock
    private UserDao userDao;
    @Mock
    private GiftCertificateDao certificateDao;
    @Mock
    private OrderDao orderDao;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void placeOrderShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        OrderDto orderDto = new OrderDto(2, 10, 100.9,
                LocalDateTime.parse("2005-10-11T19:01:30"));

        User user = new User("Fedor");
        user.setId(2);
        GiftCertificate certificate = new GiftCertificate("Free ride",
                "nice gift", 12.7, 10,
                LocalDateTime.parse("2005-10-11T19:01:30"), LocalDateTime.parse("2005-10-11T19:01:30"));
        certificate.setId(10);
        Mockito.when(userDao.findById(orderDto.getUserId())).thenReturn(Optional.of(user));
        Mockito.when(certificateDao.findById(orderDto.getCertificateId())).thenReturn(Optional.of(certificate));
        Mockito.when(orderDao.create(any())).thenReturn(14);

        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);
        OrderDto actual = orderService.placeOrder(orderDto);
        orderDto.setId(14);

        boolean comparisonResult = actual.getId() == orderDto.getId() && actual.getUserId() == orderDto.getUserId()
                && actual.getCertificateId() == orderDto.getCertificateId()
                && Double.compare(actual.getTotal(), orderDto.getTotal()) == 0;
        Assertions.assertTrue(comparisonResult);
    }

    @Test
    public void placeOrderShouldThrowNoSuchElementException() throws DaoException {
        OrderDto orderDto = new OrderDto(10, 120, 650.9,
                LocalDateTime.parse("2011-09-11T09:01:30"));

        Mockito.when(userDao.findById(orderDto.getUserId())).thenReturn(Optional.empty());
        Mockito.when(certificateDao.findById(orderDto.getCertificateId())).thenReturn(Optional.empty());

        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);
        Assertions.assertThrows(NoSuchElementException.class, () -> orderService.placeOrder(orderDto));
    }

    @Test
    public void placeOrderShouldThrowServiceException() throws DaoException {
        OrderDto orderDto = new OrderDto(10, 120, 650.9,
                LocalDateTime.parse("2011-09-11T09:01:30"));

        Mockito.when(userDao.findById(anyInt())).thenThrow(DaoException.class);

        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);
        Assertions.assertThrows(ServiceException.class, () -> orderService.placeOrder(orderDto));
    }

    @Test
    public void getUserOrdersShouldBeCorrect() throws DaoException, ServiceException, InvalidRequestDataException {
        Order order1 = new Order(10, 100, 650.9,
                LocalDateTime.parse("2011-09-11T09:01:30"));
        Order order2 = new Order(10, 9, 12.9,
                LocalDateTime.parse("2013-12-11T21:19:39"));

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        Mockito.when(orderDao.getUserOrders(10, 2, 0)).thenReturn(orders);
        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);

        List<OrderDto> expected = orders.stream().map(t -> conversionService.convert(t, OrderDto.class))
                .collect(Collectors.toList());
        List<OrderDto> actual = orderService.getUserOrders(10, 0, 2);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUserOrdersShouldThrowServiceException() throws DaoException {
        Mockito.when(orderDao.getUserOrders(5, 10, 0)).thenThrow(DaoException.class);
        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> orderService.getUserOrders(5, 0, 10));
    }

    @Test
    public void getOrderCountShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        User user = new User("Frederick");
        user.setId(7);

        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(orderDao.getUserOrderCount(7)).thenReturn(101L);
        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);

        long expected = 101;
        long actual = orderService.getUserOrderCount(7);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getOrderCountShouldThrowNoSuchElementException() throws DaoException {
        Mockito.when(userDao.findById(anyInt())).thenReturn(Optional.empty());
        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);

        Assertions.assertThrows(NoSuchElementException.class, () -> orderService.getUserOrderCount(11));
    }

    @Test
    public void getOrderCountShouldThrowServiceException() throws DaoException {
        User user = new User("Melissa");
        user.setId(19);

        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(orderDao.getUserOrderCount(user.getId())).thenThrow(DaoException.class);
        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> orderService.getUserOrderCount(19));
    }
}
