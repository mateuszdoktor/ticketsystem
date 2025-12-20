package com.example.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket extends AuditableEntity {
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
        return this.createdBy.getId().equals(user.getId());
    }

    public boolean canBeManagedBy(User user) {
        return user.getUserRole().canManageTickets() || this.isCreatedBy(user);
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
