package com.example.ticketsystem.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.ticketsystem.entity.Ticket;
import com.example.ticketsystem.entity.User;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    Page<Ticket> findByCreatedBy(User createdBy, Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy", "assignedTo", "comments", "comments.author"})
    @Query("select t from Ticket t where t.id = :id")
    Optional<Ticket> findDetailedById(Long id);
}
