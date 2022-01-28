package com.epam.esm.web.model.mapper.impl;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.model.UserRequestModel;
import com.epam.esm.web.model.mapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserModelMapper implements ModelMapper<UserDto, UserRequestModel> {

    @Override
    public UserDto toDto(UserRequestModel requestModel) {
        UserDto userDto = new UserDto(requestModel.getName());
        userDto.setId(requestModel.getId());
        return userDto;
    }

    @Override
    public UserRequestModel toRequestModel(UserDto dto) {
        UserRequestModel userModel = new UserRequestModel(dto.getId(), dto.getName());
        return userModel;
    }
}
