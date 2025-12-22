package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.comment.CommentCreateDto;
import com.example.ticketsystem.dto.comment.CommentResponseDto;
import com.example.ticketsystem.dto.ticket.*;
import com.example.ticketsystem.entity.Comment;
import com.example.ticketsystem.entity.Ticket;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.CommentMapper;
import com.example.ticketsystem.mapper.TicketMapper;
import com.example.ticketsystem.service.CommentService;
import com.example.ticketsystem.service.TicketService;
import com.example.ticketsystem.util.PaginationUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    public TicketController(TicketService ticketService, TicketMapper ticketMapper, CommentMapper commentMapper, CommentService commentService) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(@Valid @RequestBody TicketCreateDto ticketCreateDto, @AuthenticationPrincipal User user) {
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
    public ResponseEntity<Page<TicketListDto>> getMyTickets(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<Ticket> page = ticketService.findMyTickets(user, pageable);
        Page<TicketListDto> dtoPage = page.map(ticketMapper::toListDto);

        HttpHeaders httpHeaders = PaginationUtil.buildPaginationHeaders(dtoPage);

        return ResponseEntity.ok().headers(httpHeaders).body(dtoPage);
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<TicketResponseDto> assignTicket(@PathVariable Long id, @AuthenticationPrincipal User user, @Valid @RequestBody TicketAssignRequestDto ticketAssignRequestDto) {
        Ticket ticket = ticketService.assignTicket(id, ticketAssignRequestDto.getUserId(), user);
        TicketResponseDto ticketResponseDto = ticketMapper.toResponseDto(ticket);

        return ResponseEntity.ok(ticketResponseDto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDto> changeTicketStatus(@PathVariable Long id, @AuthenticationPrincipal User user, @Valid @RequestBody TicketStatusUpdateRequestDto ticketStatusUpdateRequestDto) {
        Ticket ticket = ticketService.changeStatus(id, ticketStatusUpdateRequestDto.getStatus(), user);
        TicketResponseDto ticketResponseDto = ticketMapper.toResponseDto(ticket);

        return ResponseEntity.ok(ticketResponseDto);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDto> addCommentToTicket(@PathVariable Long id, @AuthenticationPrincipal User user, @Valid @RequestBody CommentCreateDto commentCreateDto) {
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
