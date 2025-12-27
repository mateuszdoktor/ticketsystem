package com.example.ticketsystem.service;

import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.exceptions.ticket.TicketNotFoundException;
import com.example.ticketsystem.exceptions.user.UserNotFoundException;
import com.example.ticketsystem.repository.CommentRepository;
import com.example.ticketsystem.repository.TicketRepository;
import com.example.ticketsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<Comment> findByTicketId(Long ticketId, Pageable pageable) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException("Ticket with id: " + ticketId + ", not found");
        }
        return commentRepository.findByTicketId(ticketId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Comment> findByAuthorId(Long authorId, Pageable pageable) {
        if (!userRepository.existsById(authorId)) {
            throw new UserNotFoundException("User with id: " + authorId + ", not found");
        }
        return commentRepository.findByAuthorId(authorId, pageable);
    }
}
