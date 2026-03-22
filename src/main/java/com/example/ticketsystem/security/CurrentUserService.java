package com.example.ticketsystem.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.exceptions.security.AccessDeniedException;
import com.example.ticketsystem.service.UserService;

@Service
public class CurrentUserService {

    private final UserService userService;

    public CurrentUserService(UserService userService) {
        this.userService = userService;
    }

    public Long extractUserId(Jwt jwt) {
        Object userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            throw new AccessDeniedException("Missing userId claim in token");
        }

        if (userIdClaim instanceof Number number) {
            return number.longValue();
        }

        if (userIdClaim instanceof String userIdAsString) {
            try {
                return Long.valueOf(userIdAsString);
            } catch (NumberFormatException ex) {
                throw new AccessDeniedException("Invalid userId claim in token");
            }
        }

        throw new AccessDeniedException("Invalid userId claim in token");
    }

    public User getCurrentUser(Jwt jwt) {
        return userService.findById(extractUserId(jwt));
    }
}
