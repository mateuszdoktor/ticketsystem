package com.example.ticketsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import com.example.ticketsystem.repository.UserRepository;
import com.example.ticketsystem.security.JwtService;
import com.example.ticketsystem.security.MyUserDetails;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void getUser_ShouldReturn200_WhenUserRequestsOwnProfile() throws Exception {
        User user = persistUser("user-self", UserRole.ROLE_USER);

        mockMvc.perform(get("/api/users/{id}", user.getId())
                        .header("Authorization", bearerToken(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    void getUsers_ShouldReturn401_WhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUsers_ShouldReturn403_WhenRoleUserRequestsAdminEndpoint() throws Exception {
        User user = persistUser("user-non-admin", UserRole.ROLE_USER);

        mockMvc.perform(get("/api/users")
                        .header("Authorization", bearerToken(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUser_ShouldReturn404_WhenAdminRequestsMissingUser() throws Exception {
        User admin = persistUser("user-admin", UserRole.ROLE_ADMIN);

        mockMvc.perform(get("/api/users/{id}", 999999L)
                        .header("Authorization", bearerToken(admin)))
                .andExpect(status().isNotFound());
    }

    private String bearerToken(User user) {
        return "Bearer " + jwtService.generateToken(new MyUserDetails(user));
    }

    private User persistUser(String username, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setUserRole(role);
        return userRepository.save(user);
    }
}
