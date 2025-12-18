package com.example.ticketsystem.repository;

import com.example.ticketsystem.entity.Comment;
import com.example.ticketsystem.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTicketOrderByCreatedAt(Ticket ticket, Pageable pageable);
}
