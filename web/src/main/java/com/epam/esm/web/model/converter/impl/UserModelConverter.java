package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.model.UserRequestModel;
import com.epam.esm.web.model.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class UserModelConverter extends AbstractTwoWayConverter<UserRequestModel, UserDto> {

    @Override
    protected UserDto convertTo(UserRequestModel source) {
        UserDto userDto = new UserDto();
        try {
            BeanUtils.copyProperties(userDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // ignore :(
            e.printStackTrace();
        }
        return userDto;
    }

    @Override
    protected UserRequestModel convertBack(UserDto source) {
        UserRequestModel userModel = new UserRequestModel();
        try {
            BeanUtils.copyProperties(userModel, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return userModel;
    }
}
