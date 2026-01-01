package com.example.ticketsystem.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.ticketsystem.dto.user.UserCreateDto;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import com.example.ticketsystem.exceptions.user.UserAlreadyExistsException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
import com.example.ticketsystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Nested
    class WhenFindingUser {

        @Test
        void findById_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
            Long userId = 1L;
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.findById(userId));

            then(userRepository).should().findById(userId);
        }

        @Test
        void findAll_ShouldDelegateToRepositoryAndReturnPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> expectedPage = new PageImpl<>(List.of(new User()));

            given(userRepository.findAll(pageable)).willReturn(expectedPage);

            Page<User> result = userService.findAll(pageable);

            assertSame(expectedPage, result);
            then(userRepository).should().findAll(pageable);
        }
    }

    @Nested
    class WhenCreatingUser {

        private UserCreateDto userCreateDto;

        @BeforeEach
        void setup() {
            userCreateDto = new UserCreateDto();
            userCreateDto.setUsername("testuser");
            userCreateDto.setPassword("password123");
            userCreateDto.setRole(UserRole.ROLE_ADMIN);
        }

        @Test
        void createUser_WhenValidDto_ShouldSaveUserWithEncodedPassword() {
            String encodedPassword = "encoded-password123";
            given(bCryptPasswordEncoder.encode(userCreateDto.getPassword())).willReturn(encodedPassword);
            given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

            User result = userService.createUser(userCreateDto);

            assertEquals(encodedPassword, result.getPassword());
            then(bCryptPasswordEncoder).should().encode("password123");
            then(userRepository).should().save(any(User.class));
        }

        @Test
        void createUser_WhenUsernameAlreadyExists_ShouldThrowUserAlreadyExistsException() {
            given(userRepository.save(any(User.class))).willThrow(DataIntegrityViolationException.class);

            assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDto));
        }
    }
}
