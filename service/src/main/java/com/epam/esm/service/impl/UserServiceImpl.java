package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.DaoException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.impl.TagDtoMapper;
import com.epam.esm.service.dto.mapper.impl.UserDtoMapper;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.service.validator.QueryParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    public static final int USER_CODE = 3;

    private UserDao userDao;
    private TagDao tagDao;

    private UserDtoMapper userMapper;
    private TagDtoMapper tagMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, TagDao tagDao,
                           UserDtoMapper userMapper, TagDtoMapper tagMapper) {
        this.userDao = userDao;
        this.tagDao = tagDao;
        this.userMapper = userMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public UserDto getUser(int id) throws ServiceException, NoSuchElementException {
        try {
            Optional<User> user = userDao.findById(id);
            if (!user.isPresent()) {
                throw new NoSuchElementException("Unable to get user (id = " + id + ")", USER_CODE);
            }
            return userMapper.toDto(user.get());
        } catch (DaoException e) {
            throw new ServiceException("Unable to get user (id = " + id + ")", e, USER_CODE);
        }
    }

    @Override
    public List<UserDto> getUsers(String page, String size) throws ServiceException, InvalidRequestDataException {
        QueryParamValidator validator = new QueryParamValidator();
        if (!validator.validatePositiveInteger(page) || !validator.validatePositiveInteger(size)) {
            throw new InvalidRequestDataException("Invalid pagination parameters", USER_CODE);
        }
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);
        try {
            List<User> users = userDao.getUsers(pageSize, pageNumber * pageSize);
            return userMapper.toDtoList(users);

        } catch (DaoException e) {
            throw new ServiceException("Unable to get users", e, USER_CODE);
        }
    }
}
