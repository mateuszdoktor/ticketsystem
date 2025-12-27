package com.example.ticketsystem.dto.user;

import com.example.ticketsystem.entity.user.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private UserRole role;
}
