package com.example.ticketsystem.repository;

import com.example.ticketsystem.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"author"})
    Page<Comment> findByTicketId(Long ticketId, Pageable pageable);

    Page<Comment> findByAuthorId(Long userId, Pageable pageable);
}
