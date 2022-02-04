package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    public static final int USER_CODE = 3;

    private UserDao userDao;
    private ConversionService conversionService;

    @Autowired
    public UserServiceImpl(UserDao userDao, ConversionService conversionService) {
        this.userDao = userDao;
        this.conversionService = conversionService;
    }

    @Override
    public UserDto getUser(int id) throws ServiceException, NoSuchElementException {
        try {
            Optional<User> user = userDao.findById(id);
            if (!user.isPresent()) {
                throw new NoSuchElementException("Unable to get user (id = " + id + ")", USER_CODE);
            }
            return conversionService.convert(user.get(), UserDto.class);
        } catch (DaoException e) {
            throw new ServiceException("Unable to get user (id = " + id + ")", e, USER_CODE);
        }
    }

    @Override
    public List<UserDto> getUsers(int pageNumber, int pageSize) throws ServiceException {
        try {
            List<User> users = userDao.getUsers(pageSize, pageNumber * pageSize);
            return users.stream().map(u -> conversionService.convert(u, UserDto.class)).collect(Collectors.toList());

        } catch (DaoException e) {
            throw new ServiceException("Unable to get users", e, USER_CODE);
        }
    }

    @Override
    public long getUserCount() throws ServiceException {
        try {
            return userDao.getCount();
        } catch (DaoException e) {
            throw new ServiceException("Unable to count all users", e, USER_CODE);
        }
    }
}
