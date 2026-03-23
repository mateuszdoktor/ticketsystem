DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'chk_users_role'
          AND conrelid = 'users'::regclass
    ) THEN
        ALTER TABLE users
            ADD CONSTRAINT chk_users_role
            CHECK (role IN ('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN'));
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'chk_tickets_status'
          AND conrelid = 'tickets'::regclass
    ) THEN
        ALTER TABLE tickets
            ADD CONSTRAINT chk_tickets_status
            CHECK (status IN ('NEW', 'IN_PROGRESS', 'CLOSED'));
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'chk_tickets_priority'
          AND conrelid = 'tickets'::regclass
    ) THEN
        ALTER TABLE tickets
            ADD CONSTRAINT chk_tickets_priority
            CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH'));
    END IF;
END $$;
