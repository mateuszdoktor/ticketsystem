package com.example.ticketsystem.service;

import com.example.ticketsystem.entity.Comment;
import com.example.ticketsystem.exceptions.ticket.TicketNotFoundException;
import com.example.ticketsystem.repository.CommentRepository;
import com.example.ticketsystem.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public Page<Comment> findCommentsForTicket(Long ticketId, Pageable pageable) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException("Ticket with id: " + ticketId + ", not found");
        }

        return commentRepository.findByTicketId(ticketId, pageable);
    }
}
