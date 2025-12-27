package com.example.ticketsystem.specification;

import com.example.ticketsystem.dto.ticket.TicketFilterObject;
import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.entity.ticket.TicketPriority;
import com.example.ticketsystem.entity.ticket.TicketStatus;
import com.example.ticketsystem.entity.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TicketSpecifications {
    public static Specification<Ticket> status(TicketStatus status) {
        if (status == null) return null;

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Ticket> priority(TicketPriority priority) {
        if (priority == null) return null;

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Ticket> createdBy(Long createdById) {
        if (createdById == null) return null;

        return (root, query, criteriaBuilder) -> {
            Join<Ticket, User> user = root.join("createdBy");
            return criteriaBuilder.equal(user.get("id"), createdById);
        };
    }

    public static Specification<Ticket> assignedTo(Long assignedToId) {
        if (assignedToId == null) return null;

        return (root, query, criteriaBuilder) -> {
            Join<Ticket, User> user = root.join("assignedTo", JoinType.LEFT);
            return criteriaBuilder.equal(user.get("id"), assignedToId);
        };
    }

    public static Specification<Ticket> createdAtFrom(LocalDateTime dateTime) {
        if (dateTime == null) return null;

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), dateTime);
    }

    public static Specification<Ticket> createdAtTo(LocalDateTime dateTime) {
        if (dateTime == null) return null;

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), dateTime);
    }

    public static Specification<Ticket> fromFilter(TicketFilterObject f) {
        return Specification.allOf(
                status(f.getStatus()),
                priority(f.getPriority()),
                createdBy(f.getCreatedById()),
                assignedTo(f.getAssignedToId()),
                createdAtFrom(f.getCreatedAtFrom()),
                createdAtTo(f.getCreatedAtTo()));
    }
}
