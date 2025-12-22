package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.comment.CommentResponseDto;
import com.example.ticketsystem.dto.ticket.TicketListDto;
import com.example.ticketsystem.dto.user.UserCreateDto;
import com.example.ticketsystem.dto.user.UserResponseDto;
import com.example.ticketsystem.entity.Comment;
import com.example.ticketsystem.entity.Ticket;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.CommentMapper;
import com.example.ticketsystem.mapper.TicketMapper;
import com.example.ticketsystem.mapper.UserMapper;
import com.example.ticketsystem.service.CommentService;
import com.example.ticketsystem.service.TicketService;
import com.example.ticketsystem.service.UserService;
import com.example.ticketsystem.util.PaginationUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public UserController(UserService userService, UserMapper userMapper, TicketService ticketService, TicketMapper ticketMapper, CommentService commentService, CommentMapper commentMapper) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.userMapper = userMapper;
        this.ticketMapper = ticketMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        User user = userService.createUser(userCreateDto);
        UserResponseDto userResponseDto = userMapper.toResponseDto(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userResponseDto.getId()).toUri();

        return ResponseEntity.created(location).body(userResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponseDto userResponseDto = userMapper.toResponseDto(user);

        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping()
    public ResponseEntity<Page<UserResponseDto>> getUsers(Pageable pageable) {
        Page<User> page = userService.findAll(pageable);
        Page<UserResponseDto> dtoPage = page.map(userMapper::toResponseDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }

    @GetMapping("/{id}/created-tickets")
    public ResponseEntity<Page<TicketListDto>> getUserCreatedTickets(@PathVariable Long id, Pageable pageable) {
        Page<Ticket> page = ticketService.findByCreatedById(id, pageable);
        Page<TicketListDto> dtoPage = page.map(ticketMapper::toListDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }

    @GetMapping("/{id}/assigned-tickets")
    public ResponseEntity<Page<TicketListDto>> getUserAssignedTickets(@PathVariable Long id, Pageable pageable) {
        Page<Ticket> page = ticketService.findByAssignedToId(id, pageable);
        Page<TicketListDto> dtoPage = page.map(ticketMapper::toListDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getUserComments(@PathVariable Long id, Pageable pageable) {
        Page<Comment> page = commentService.findByAuthorId(id, pageable);
        Page<CommentResponseDto> dtoPage = page.map(commentMapper::toResponseDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }
}
