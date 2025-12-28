package com.example.ticketsystem.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
