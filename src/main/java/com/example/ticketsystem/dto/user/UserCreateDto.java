package com.example.ticketsystem.dto.user;

import com.example.ticketsystem.entity.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank
    @Size(max = 25)
    private String username;
    @NotBlank
    @Size(max = 25)
    private String password;
    @NotNull
    private UserRole role;
}
