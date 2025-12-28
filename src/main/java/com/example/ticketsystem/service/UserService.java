package com.example.ticketsystem.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticketsystem.dto.user.UserCreateDto;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.exceptions.user.UserAlreadyExistsException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
import com.example.ticketsystem.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public User createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setUserRole(userCreateDto.getRole());
        user.setPassword(bCryptPasswordEncoder.encode(userCreateDto.getPassword()));

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException("User with username: " + userCreateDto.getUsername() + ", already exists.", exception);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.claims['userId']")
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + ", not found."));
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username: " + username + ", not found."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
