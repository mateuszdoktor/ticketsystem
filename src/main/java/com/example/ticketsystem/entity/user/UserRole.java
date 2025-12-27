package com.example.ticketsystem.entity;

public enum UserRole {
    ROLE_USER, ROLE_MANAGER, ROLE_ADMIN;

    public boolean canManageTickets() {
        return this == ROLE_ADMIN || this == ROLE_MANAGER;
    }
}
