package com.example.ticketsystem.entity.ticket;

import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    User user = null;
    Ticket ticket = null;
    Comment comment = null;

    @BeforeEach
    void setup() {
        user = new User();
        ticket = new Ticket();
        comment = new Comment();
    }

    @Test
    void canBeManagedBy_RoleAdminOrManager_ShouldReturnTrue() {
        user.setUserRole(UserRole.ROLE_ADMIN);
        assertTrue(ticket.canBeManagedBy(user));

        user.setUserRole(UserRole.ROLE_MANAGER);
        assertTrue(ticket.canBeManagedBy(user));
    }

    @Test
    void canBeManagedBy_RoleUserNotCreatedBy_ShouldReturnFalse() {
        user.setUserRole(UserRole.ROLE_USER);
        user.setId(1L);

        User ticketAuthor = new User();
        ticketAuthor.setId(2L);

        ticket.setCreatedBy(ticketAuthor);

        assertFalse(ticket.canBeManagedBy(user));
    }

    @Test
    void canBeManagedBy_CreatedByUser_ShouldReturnTrue() {
        ticket.setCreatedBy(user);
        user.setId(1L);
        assertTrue(ticket.canBeManagedBy(user));
    }

    @Test
    void addComment_AssociatesCommentAndTicket() {
        ticket.addComment(comment);
        assertTrue(ticket.getComments().contains(comment));
        assertSame(ticket, comment.getTicket());
    }

}
