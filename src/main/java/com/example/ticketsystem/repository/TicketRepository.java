package com.example.ticketsystem.repository;

import com.example.ticketsystem.entity.Ticket;
import com.example.ticketsystem.entity.TicketPriority;
import com.example.ticketsystem.entity.TicketStatus;
import com.example.ticketsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByPriority(TicketPriority priority, Pageable pageable);

    Page<Ticket> findByCreatedBy(User createdBy, Pageable pageable);

    Page<Ticket> findByAssignedTo(User assignedTo, Pageable pageable);

    Page<Ticket> findByStatusAndAssignedTo(TicketStatus status, User assignedTo, Pageable pageable);

    Page<Ticket> findByStatusAndCreatedBy(TicketStatus status, User createdBy, Pageable pageable);

    Page<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority, Pageable pageable);
}
