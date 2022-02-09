package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

public class UserDtoConverter extends AbstractTwoWayConverter<UserDto, User> {

    private final Logger logger = LoggerFactory.getLogger(UserDtoConverter.class);

    @Override
    protected User convertTo(UserDto source) {
        User user = new User();
        try {
            BeanUtils.copyProperties(user, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from UserDto to User", e);
        }
        return user;
    }

    @Override
    protected UserDto convertBack(User source) {
        UserDto userDto = new UserDto();
        try {
            BeanUtils.copyProperties(userDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from User to UserDto", e);
        }
        return userDto;
    }
}
