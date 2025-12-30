package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.user.UserCreateDto;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import com.example.ticketsystem.exceptions.user.UserAlreadyExistsException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
import com.example.ticketsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserService userService;

    UserCreateDto userCreateDto = null;

    @Nested
    class WhenFindingUser {

        @Test
        void findById_UserDoesNotExist_ShouldThrowUserNotFoundException() {
            Long id = 1L;
            given(userRepository.findById(id)).willReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.findById(id));

            then(userRepository).should().findById(id);
        }

        @Test
        void findAll_ShouldDelegateToRepositoryAndReturnPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> repoPage = new PageImpl<>(List.of(new User()));

            given(userRepository.findAll(pageable)).willReturn(repoPage);

            Page<User> result = userService.findAll(pageable);
            assertSame(repoPage, result);

            then(userRepository).should().findAll(pageable);
        }
    }

    @Nested
    class WhenCreatingUser {

        @BeforeEach
        void setup() {
            userCreateDto = new UserCreateDto();
            userCreateDto.setUsername("test");
            userCreateDto.setPassword("test");
            userCreateDto.setRole(UserRole.ROLE_ADMIN);
        }

        @Test
        void createUser_ValidDto_ShouldSaveUserWithEncodedPassword() {
            String encodedPassword = "encoded-test";
            given(bCryptPasswordEncoder.encode(userCreateDto.getPassword())).willReturn(encodedPassword);

            given(userRepository.save(any(User.class))).willAnswer((invocation) -> invocation.<User>getArgument(0));

            User result = userService.createUser(userCreateDto);

            assertEquals(encodedPassword, result.getPassword());

            then(bCryptPasswordEncoder).should().encode("test");
            then(userRepository).should().save(any(User.class));
        }

        @Test
        void createUser_UsernameAlreadyExists_ShouldThrowUserAlreadyExistsException() {
            given(userRepository.save(any(User.class))).willThrow(DataIntegrityViolationException.class);

            assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDto));
        }
    }
}
