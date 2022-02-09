package com.epam.esm.web.model.converter.impl;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.model.UserRequestModel;
import com.epam.esm.service.dto.converter.AbstractTwoWayConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

public class UserModelConverter extends AbstractTwoWayConverter<UserRequestModel, UserDto> {

    private final Logger logger = LoggerFactory.getLogger(UserModelConverter.class);

    @Override
    protected UserDto convertTo(UserRequestModel source) {
        UserDto userDto = new UserDto();
        try {
            BeanUtils.copyProperties(userDto, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from UserRequestModel to UserDto", e);
        }
        return userDto;
    }

    @Override
    protected UserRequestModel convertBack(UserDto source) {
        UserRequestModel userModel = new UserRequestModel();
        try {
            BeanUtils.copyProperties(userModel, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error while converting from UserDto to UserRequestModel", e);
        }
        return userModel;
    }
}
