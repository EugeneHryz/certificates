package com.epam.esm.service.impl;

import com.epam.esm.repository.config.PersistenceConfig;
import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.config.TestConfig;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PersistenceConfig.class, TestConfig.class})
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
        User user = new User("Fedor");
        user.setId(2);
        GiftCertificate certificate = new GiftCertificate("Free ride",
                "nice gift", BigDecimal.valueOf(12.7), 10,
                LocalDateTime.parse("2005-10-11T19:01:30"), LocalDateTime.parse("2005-10-11T19:01:30"));
        certificate.setId(10);

        OrderDto orderDto = new OrderDto(user.getId(), certificate.getId(), certificate.getPrice(),
                LocalDateTime.parse("2005-10-11T19:01:30"));
        orderDto.setId(14);

        Mockito.when(userDao.findById(orderDto.getUserId())).thenReturn(Optional.of(user));
        Mockito.when(certificateDao.findById(orderDto.getCertificateId())).thenReturn(Optional.of(certificate));
        Mockito.when(orderDao.create(any())).thenReturn(14);

        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);
        OrderDto actual = orderService.placeOrder(orderDto);

        boolean comparisonResult = actual.getId() == orderDto.getId() && actual.getUserId() == orderDto.getUserId()
                && actual.getCertificateId() == orderDto.getCertificateId()
                && actual.getTotal().compareTo(orderDto.getTotal()) == 0;
        Assertions.assertTrue(comparisonResult);
    }

    @Test
    public void placeOrderShouldThrowNoSuchElementException() throws DaoException {
        OrderDto orderDto = new OrderDto(10, 120, BigDecimal.valueOf(650.9),
                LocalDateTime.parse("2011-09-11T09:01:30"));

        Mockito.when(userDao.findById(orderDto.getUserId())).thenReturn(Optional.empty());
        Mockito.when(certificateDao.findById(orderDto.getCertificateId())).thenReturn(Optional.empty());

        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);
        Assertions.assertThrows(NoSuchElementException.class, () -> orderService.placeOrder(orderDto));
    }

    @Test
    public void placeOrderShouldThrowServiceException() throws DaoException {
        OrderDto orderDto = new OrderDto(10, 120, BigDecimal.valueOf(650.9),
                LocalDateTime.parse("2011-09-11T09:01:30"));

        Mockito.when(userDao.findById(anyInt())).thenThrow(DaoException.class);

        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);
        Assertions.assertThrows(ServiceException.class, () -> orderService.placeOrder(orderDto));
    }

    @Test
    public void getUserOrderShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        User user = new User("Jonathan");
        user.setId(9);
        GiftCertificate certificate = new GiftCertificate("new c", "great for everyone",
                BigDecimal.valueOf(120.0), 29, LocalDateTime.now(), LocalDateTime.now());
        certificate.setId(16);
        Order order = new Order(user, certificate, BigDecimal.valueOf(134.1),
                LocalDateTime.parse("2020-11-11T11:18:30"));
        order.setId(3);

        Mockito.when(orderDao.findById(3)).thenReturn(Optional.of(order));
        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);

        OrderDto expected = conversionService.convert(order, OrderDto.class);
        OrderDto actual = orderService.getUserOrder(9, 3);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUserOrdersShouldBeCorrect() throws DaoException, ServiceException, InvalidRequestDataException {
        User user = new User("Jonathan");
        user.setId(7);
        GiftCertificate certificate1 = new GiftCertificate("new c", "great for everyone",
                BigDecimal.valueOf(120.0), 78, LocalDateTime.parse("2010-09-11T09:01:30"), LocalDateTime.parse("2010-09-11T09:01:30"));
        certificate1.setId(16);
        GiftCertificate certificate2 = new GiftCertificate("building", "great for everyone",
                BigDecimal.valueOf(190.0), 29, LocalDateTime.parse("2009-09-11T09:01:30"), LocalDateTime.parse("2009-09-11T09:01:30"));
        certificate2.setId(18);

        Order order1 = new Order(user, certificate1, certificate1.getPrice(),
                LocalDateTime.parse("2011-09-11T09:01:30"));
        Order order2 = new Order(user, certificate2, certificate2.getPrice(),
                LocalDateTime.parse("2013-12-11T21:19:39"));

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        Mockito.when(orderDao.getUserOrders(user.getId(), 2, 0)).thenReturn(orders);
        OrderService orderService = new OrderServiceImpl(orderDao, userDao, certificateDao, conversionService);

        List<OrderDto> expected = orders.stream().map(t -> conversionService.convert(t, OrderDto.class))
                .collect(Collectors.toList());
        List<OrderDto> actual = orderService.getUserOrders(user.getId(), 0, 2);
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
