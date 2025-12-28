package com.example.ticketsystem.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.ticketsystem.dto.comment.CommentCreateDto;
import com.example.ticketsystem.dto.comment.CommentResponseDto;
import com.example.ticketsystem.dto.ticket.TicketAssignRequestDto;
import com.example.ticketsystem.dto.ticket.TicketCreateDto;
import com.example.ticketsystem.dto.ticket.TicketFilterObject;
import com.example.ticketsystem.dto.ticket.TicketListDto;
import com.example.ticketsystem.dto.ticket.TicketResponseDto;
import com.example.ticketsystem.dto.ticket.TicketStatusUpdateRequestDto;
import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.mapper.CommentMapper;
import com.example.ticketsystem.mapper.TicketMapper;
import com.example.ticketsystem.service.CommentService;
import com.example.ticketsystem.service.TicketService;
import com.example.ticketsystem.service.UserService;
import com.example.ticketsystem.util.PaginationUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final UserService userService;

    public TicketController(TicketService ticketService, TicketMapper ticketMapper, CommentMapper commentMapper, CommentService commentService, UserService userService) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(@Valid @RequestBody TicketCreateDto ticketCreateDto, 
                                                          @AuthenticationPrincipal Jwt jwt) {
        var user = userService.findByUsername(jwt.getSubject());
        Ticket ticket = ticketService.createTicket(ticketCreateDto, user);
        TicketResponseDto ticketResponseDto = ticketMapper.toResponseDto(ticket);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ticketResponseDto.getId()).toUri();

        return ResponseEntity.created(location).body(ticketResponseDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id);
        TicketResponseDto ticketResponseDto = ticketMapper.toResponseDto(ticket);

        return ResponseEntity.ok(ticketResponseDto);
    }


    @GetMapping()
    public ResponseEntity<Page<TicketListDto>> getFilteredTickets(@ModelAttribute TicketFilterObject ticketFilterObject, Pageable pageable) {
        Page<Ticket> page = ticketService.searchTickets(ticketFilterObject, pageable);
        Page<TicketListDto> dtoPage = page.map(ticketMapper::toListDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<TicketListDto>> getMyTickets(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        var user = userService.findByUsername(jwt.getSubject());
        Page<Ticket> page = ticketService.findMyTickets(user, pageable);
        Page<TicketListDto> dtoPage = page.map(ticketMapper::toListDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<TicketResponseDto> assignTicket(@PathVariable Long id, 
                                                          @AuthenticationPrincipal Jwt jwt, 
                                                          @Valid @RequestBody TicketAssignRequestDto ticketAssignRequestDto) {
        var user = userService.findByUsername(jwt.getSubject());
        Ticket ticket = ticketService.assignTicket(id, ticketAssignRequestDto.getUserId(), user);
        TicketResponseDto ticketResponseDto = ticketMapper.toResponseDto(ticket);

        return ResponseEntity.ok(ticketResponseDto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDto> changeTicketStatus(@PathVariable Long id, 
                                                                 @AuthenticationPrincipal Jwt jwt, 
                                                                 @Valid @RequestBody TicketStatusUpdateRequestDto ticketStatusUpdateRequestDto) {
        var user = userService.findByUsername(jwt.getSubject());
        Ticket ticket = ticketService.changeStatus(id, ticketStatusUpdateRequestDto.getStatus(), user);
        TicketResponseDto ticketResponseDto = ticketMapper.toResponseDto(ticket);

        return ResponseEntity.ok(ticketResponseDto);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDto> addCommentToTicket(@PathVariable Long id, 
                                                                  @AuthenticationPrincipal Jwt jwt, 
                                                                  @Valid @RequestBody CommentCreateDto commentCreateDto) {
        var user = userService.findByUsername(jwt.getSubject());
        Comment comment = ticketService.addComment(id, commentCreateDto, user);
        CommentResponseDto commentResponseDto = commentMapper.toResponseDto(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsForTicket(@PathVariable Long id, Pageable pageable) {
        Page<Comment> page = commentService.findByTicketId(id, pageable);
        Page<CommentResponseDto> dtoPage = page.map(commentMapper::toResponseDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }
}
