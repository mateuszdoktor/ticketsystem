package com.example.ticketsystem.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.ticketsystem.entity.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @EntityGraph(attributePaths = {"createdBy", "assignedTo"})
    Page<Ticket> findByCreatedById(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy", "assignedTo"})
    Page<Ticket> findByAssignedToId(Long userId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"createdBy", "assignedTo"})
    Page<Ticket> findAll(Specification<Ticket> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy", "assignedTo", "comments", "comments.author"})
    @Query("select t from Ticket t where t.id = :id")
    Optional<Ticket> findDetailedById(Long id);
}
