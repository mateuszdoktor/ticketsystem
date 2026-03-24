package com.example.ticketsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import com.example.ticketsystem.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void login_ShouldReturn200AndToken_WhenCredentialsAreValid() throws Exception {
        persistUser("auth-user", "password123", UserRole.ROLE_USER);

        String payload = """
            {
              "username": "auth-user",
              "password": "password123"
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    void login_ShouldReturn401_WhenCredentialsAreInvalid() throws Exception {
        persistUser("auth-user-invalid", "password123", UserRole.ROLE_USER);

        String payload = """
            {
              "username": "auth-user-invalid",
              "password": "wrong-password"
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Authentication Failed"));
    }

    private User persistUser(String username, String rawPassword, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setUserRole(role);
        return userRepository.save(user);
    }
}
