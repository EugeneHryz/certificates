package com.epam.esm.service;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;

import java.util.List;

public interface UserService {

    UserDto getUser(int id) throws ServiceException, NoSuchElementException;

    List<UserDto> getUsers(int page, int size) throws ServiceException;

    long getUserCount() throws ServiceException;
}
