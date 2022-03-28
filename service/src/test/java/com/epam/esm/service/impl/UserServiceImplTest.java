package com.epam.esm.service.impl;

import com.epam.esm.repository.config.PersistenceConfig;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.config.TestConfig;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest(classes = {PersistenceConfig.class, TestConfig.class})
public class UserServiceImplTest {

    @Autowired
    private ConversionService conversionService;

    @Mock
    private UserDao userDao;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserShouldBeCorrect() throws DaoException, ServiceException, NoSuchElementException {
        User user = new User("Nastya");
        user.setId(3);

        Mockito.when(userDao.findById(3)).thenReturn(Optional.of(user));
        UserService userService = new UserServiceImpl(userDao, conversionService);

        UserDto expected = new UserDto("Nastya");
        expected.setId(3);
        UserDto actual = userService.getUser(3);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUserShouldThrowNoSuchElementException() throws DaoException {
        Mockito.when(userDao.findById(anyInt())).thenReturn(Optional.empty());
        UserService userService = new UserServiceImpl(userDao, conversionService);

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUser(10));
    }

    @Test
    public void getUserShouldThrowServiceException() throws DaoException {
        Mockito.when(userDao.findById(anyInt())).thenThrow(DaoException.class);
        UserService userService = new UserServiceImpl(userDao, conversionService);

        Assertions.assertThrows(ServiceException.class, () -> userService.getUser(19));
    }

    @Test
    public void getUsersShouldBeCorrect() throws DaoException, ServiceException {
        User user1 = new User("Nastya");
        user1.setId(3);
        User user2 = new User("Egor");
        user2.setId(7);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Mockito.when(userDao.getUsers(2, 0)).thenReturn(users);
        UserService userService = new UserServiceImpl(userDao, conversionService);

        List<UserDto> expected = users.stream()
                .map(u -> conversionService.convert(u, UserDto.class)).collect(Collectors.toList());
        List<UserDto> actual = userService.getUsers(0, 2);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUsersShouldThrowServiceException() throws DaoException {
        Mockito.when(userDao.getUsers(10, 10)).thenThrow(DaoException.class);

        UserService userService = new UserServiceImpl(userDao, conversionService);
        Assertions.assertThrows(ServiceException.class, () -> userService.getUsers(1, 10));
    }

    @Test
    public void getUserCountShouldBeCorrect() throws DaoException, ServiceException {
        Mockito.when(userDao.getCount()).thenReturn(16L);
        UserService userService = new UserServiceImpl(userDao, conversionService);

        long expected = 16;
        long actual = userService.getUserCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUserCountShouldThrowServiceException() throws DaoException {
        Mockito.when(userDao.getCount()).thenThrow(DaoException.class);

        UserService userService = new UserServiceImpl(userDao, conversionService);
        Assertions.assertThrows(ServiceException.class, userService::getUserCount);
    }
}
