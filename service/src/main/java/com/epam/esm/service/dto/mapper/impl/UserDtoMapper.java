package com.epam.esm.service.dto.mapper.impl;

import com.epam.esm.repository.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper implements DtoMapper<User, UserDto> {

    @Override
    public User toEntity(UserDto dto) {
        User user = new User(dto.getName());
        user.setId(dto.getId());
        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        UserDto userDto = new UserDto(entity.getName());
        userDto.setId(entity.getId());
        return userDto;
    }
}
