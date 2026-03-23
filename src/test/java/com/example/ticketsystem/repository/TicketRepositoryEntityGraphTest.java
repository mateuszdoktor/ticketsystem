package com.example.ticketsystem.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

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
class TicketRepositoryEntityGraphTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void findByCreatedById_ShouldFetchCreatedByAndAssignedTo() {
        User createdBy = persistUser("creator");
        User assignedTo = persistUser("assignee");
        persistTicket("created-by-ticket", createdBy, assignedTo);

        entityManager.flush();
        entityManager.clear();

        Page<Ticket> page = ticketRepository.findByCreatedById(createdBy.getId(), PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
        Ticket ticket = page.getContent().get(0);
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();

        assertTrue(unitUtil.isLoaded(ticket, "createdBy"));
        assertTrue(unitUtil.isLoaded(ticket, "assignedTo"));
    }

    @Test
    void findByAssignedToId_ShouldFetchCreatedByAndAssignedTo() {
        User createdBy = persistUser("creator-2");
        User assignedTo = persistUser("assignee-2");
        persistTicket("assigned-to-ticket", createdBy, assignedTo);

        entityManager.flush();
        entityManager.clear();

        Page<Ticket> page = ticketRepository.findByAssignedToId(assignedTo.getId(), PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
        Ticket ticket = page.getContent().get(0);
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();

        assertTrue(unitUtil.isLoaded(ticket, "createdBy"));
        assertTrue(unitUtil.isLoaded(ticket, "assignedTo"));
    }

    @Test
    void findAllWithSpecification_ShouldFetchCreatedByAndAssignedTo() {
        User createdBy = persistUser("creator-3");
        User assignedTo = persistUser("assignee-3");
        persistTicket("spec-ticket", createdBy, assignedTo);

        entityManager.flush();
        entityManager.clear();

        Specification<Ticket> specification = (root, query, cb) -> cb.conjunction();
        Page<Ticket> page = ticketRepository.findAll(specification, PageRequest.of(0, 10));

        assertFalse(page.isEmpty());
        Ticket ticket = page.getContent().get(0);
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();

        assertTrue(unitUtil.isLoaded(ticket, "createdBy"));
        assertTrue(unitUtil.isLoaded(ticket, "assignedTo"));
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
}
