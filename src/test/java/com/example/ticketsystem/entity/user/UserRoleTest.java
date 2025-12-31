package com.example.ticketsystem.entity.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRoleTest {
    @Test
    void canManageTickets_RoleAdminOrManager_ShouldReturnTrue() {
        assertTrue(UserRole.ROLE_ADMIN.canManageTickets());
        assertTrue(UserRole.ROLE_MANAGER.canManageTickets());
    }

    @Test
    void canManageTickets_RoleUser_ShouldReturnFalse() {
        assertFalse(UserRole.ROLE_USER.canManageTickets());
    }
}
