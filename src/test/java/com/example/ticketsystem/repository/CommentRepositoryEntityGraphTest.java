package com.example.ticketsystem.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticketsystem.entity.comment.Comment;
import com.example.ticketsystem.entity.ticket.Ticket;
import com.example.ticketsystem.entity.ticket.TicketPriority;
import com.example.ticketsystem.entity.ticket.TicketStatus;
import com.example.ticketsystem.entity.user.User;
import com.example.ticketsystem.entity.user.UserRole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;

@DataJpaTest
@Transactional
class CommentRepositoryEntityGraphTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void findByAuthorId_ShouldFetchAuthor() {
        User author = persistUser("comment-author");
        Ticket ticket = persistTicket("comment-ticket", author, author);
        persistComment(ticket, author, "comment by author");

        entityManager.flush();
        entityManager.clear();

        Page<Comment> page = commentRepository.findByAuthorId(author.getId(), PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
        Comment comment = page.getContent().get(0);
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();

        assertTrue(unitUtil.isLoaded(comment, "author"));
    }

    @Test
    void findByTicketId_ShouldFetchAuthor() {
        User author = persistUser("comment-author-2");
        Ticket ticket = persistTicket("comment-ticket-2", author, author);
        persistComment(ticket, author, "comment by ticket id");

        entityManager.flush();
        entityManager.clear();

        Page<Comment> page = commentRepository.findByTicketId(ticket.getId(), PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
        Comment comment = page.getContent().get(0);
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();

        assertTrue(unitUtil.isLoaded(comment, "author"));
    }

    private User persistUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setUserRole(UserRole.ROLE_USER);
        entityManager.persist(user);
        return user;
    }

    private Ticket persistTicket(String title, User createdBy, User assignedTo) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription("description for " + title);
        ticket.setStatus(TicketStatus.NEW);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCreatedBy(createdBy);
        ticket.setAssignedTo(assignedTo);
        entityManager.persist(ticket);
        return ticket;
    }

    private Comment persistComment(Ticket ticket, User author, String text) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setTicket(ticket);
        comment.setAuthor(author);
        entityManager.persist(comment);
        return comment;
    }
}
