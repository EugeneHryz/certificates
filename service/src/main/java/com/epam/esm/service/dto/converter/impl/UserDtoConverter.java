package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class UserDtoConverter extends AbstractTwoWayConverter<UserDto, User> {

    @Override
    protected User convertTo(UserDto source) {
        User user = new User();
        try {
            BeanUtils.copyProperties(user, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // ignore
            e.printStackTrace();
        }
        return user;
    }

    @Override
    protected UserDto convertBack(User source) {
        UserDto userDto = new UserDto();
        try {
            BeanUtils.copyProperties(userDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // ignore
            e.printStackTrace();
        }
        return userDto;
    }
}
