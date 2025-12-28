package com.example.ticketsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ticketsystem.dto.user.UserResponseDto;
import com.example.ticketsystem.dto.user.UserSimpleDto;
import com.example.ticketsystem.entity.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserSimpleDto toSimpleDto(User user);

    @Mapping(target = "role", source = "userRole")
    UserResponseDto toResponseDto(User user);
}
