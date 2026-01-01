package com.example.ticketsystem.dto.user;

import com.example.ticketsystem.entity.user.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 25, message = "Username must be between 3 and 25 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscores and dashes")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 25, message = "Password must be between 8 and 25 characters")
    private String password;
    
    @NotNull(message = "Role is required")
    private UserRole role;
}
