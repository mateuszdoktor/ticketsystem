package com.example.ticketsystem.entity.ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.ticketsystem.entity.AuditableEntity;
import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.entity.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket extends AuditableEntity {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public boolean isCreatedBy(User user) {
        if (user == null || user.getId() == null || this.createdBy == null || this.createdBy.getId() == null) {
            return false;
        }
        return this.createdBy.getId().equals(user.getId());
    }

    public boolean isAssignedTo(User user) {
        if (user == null || user.getId() == null || this.assignedTo == null || this.assignedTo.getId() == null) {
            return false;
        }
        return this.assignedTo.getId().equals(user.getId());
    }

    public boolean canBeViewedBy(User user) {
        boolean hasManagementRole = user != null && user.getUserRole() != null && user.getUserRole().canManageTickets();
        return hasManagementRole || this.isCreatedBy(user) || this.isAssignedTo(user);
    }

    public boolean canBeManagedBy(User user) {
        boolean hasManagementRole = user != null && user.getUserRole() != null && user.getUserRole().canManageTickets();
        return hasManagementRole || this.isCreatedBy(user);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setTicket(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setTicket(null);
    }
}
