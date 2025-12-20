package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.comment.CommentCreateDto;
import com.example.ticketsystem.dto.ticket.TicketCreateDto;
import com.example.ticketsystem.dto.ticket.TicketFilterObject;
import com.example.ticketsystem.entity.Comment;
import com.example.ticketsystem.entity.Ticket;
import com.example.ticketsystem.entity.TicketStatus;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.exceptions.security.AccessDeniedException;
import com.example.ticketsystem.exceptions.ticket.TicketNotFoundException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
import com.example.ticketsystem.repository.TicketRepository;
import com.example.ticketsystem.repository.UserRepository;
import com.example.ticketsystem.specification.TicketSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    private Ticket getTicketOrThrow(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new TicketNotFoundException("Ticket with id: " + ticketId + ", not found"));
    }

    @Transactional
    public Ticket createTicket(TicketCreateDto ticketCreateDto, User loggedUser) {
        Ticket ticket = new Ticket();
        ticket.setTitle(ticketCreateDto.getTitle());
        ticket.setDescription(ticketCreateDto.getDescription());
        ticket.setStatus(TicketStatus.NEW);
        ticket.setPriority(ticketCreateDto.getPriority());
        ticket.setCreatedBy(loggedUser);

        if (ticketCreateDto.getAssignedToId() != null) {
            User userAssignedTo = userRepository
                    .findById(ticketCreateDto.getAssignedToId())
                    .orElseThrow(() ->
                            new UserNotFoundException("User with id: " + ticketCreateDto.getAssignedToId() + ", not found"));
            ticket.setAssignedTo(userAssignedTo);
        }

        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        return getTicketOrThrow(id);
    }

    @Transactional
    public Ticket assignTicket(Long ticketId, Long userId, User loggedUser) {
        Ticket ticket = getTicketOrThrow(ticketId);

        if (!ticket.canBeManagedBy(loggedUser)) {
            throw new AccessDeniedException("User not authorised to this operation");
        } else {
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + ", not found"));
            ticket.setAssignedTo(user);
            return ticketRepository.save(ticket);
        }
    }

    @Transactional
    public Ticket changeStatus(Long ticketId, TicketStatus newStatus, User loggedUser) {
        Ticket ticket = getTicketOrThrow(ticketId);

        if (!ticket.canBeManagedBy(loggedUser)) {
            throw new AccessDeniedException("User not authorised to this operation");
        } else {
            ticket.setStatus(newStatus);
            if (newStatus == TicketStatus.DONE) ticket.setClosedAt(LocalDateTime.now());
            return ticketRepository.save(ticket);
        }
    }

    @Transactional
    public Comment addComment(Long ticketId, CommentCreateDto commentCreateDto, User loggedUser) {
        Ticket ticket = getTicketOrThrow(ticketId);

        Comment comment = new Comment();
        comment.setText(commentCreateDto.getText());
        comment.setAuthor(loggedUser);

        ticket.addComment(comment);

        ticketRepository.save(ticket);

        return comment;
    }

    @Transactional(readOnly = true)
    public Page<Ticket> findMyTickets(User loggedUser, Pageable pageable) {
        return ticketRepository.findByCreatedBy(loggedUser, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> searchTickets(TicketFilterObject filterObject, Pageable pageable) {
        Specification<Ticket> spec = TicketSpecifications.fromFilter(filterObject);
        return ticketRepository.findAll(spec, pageable);
    }
}
