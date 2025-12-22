package com.example.ticketsystem.mapper;

import com.example.ticketsystem.dto.user.UserResponseDto;
import com.example.ticketsystem.dto.user.UserSimpleDto;
import com.example.ticketsystem.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserSimpleDto toSimpleDto(User user);

    UserResponseDto toResponseDto(User user);
}
