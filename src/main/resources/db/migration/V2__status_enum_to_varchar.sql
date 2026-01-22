-- Convert Postgres enum columns to VARCHAR so Hibernate can insert strings safely

-- 1) Drop enum-based defaults that depend on the enum type
ALTER TABLE applications
    ALTER COLUMN status DROP DEFAULT;

-- 2) Convert columns to VARCHAR
ALTER TABLE applications
ALTER COLUMN status TYPE VARCHAR(50)
    USING status::text;

ALTER TABLE application_status_history
ALTER COLUMN from_status TYPE VARCHAR(50)
    USING from_status::text;

ALTER TABLE application_status_history
ALTER COLUMN to_status TYPE VARCHAR(50)
    USING to_status::text;

-- 3) Re-add a safe VARCHAR default (optional)
ALTER TABLE applications
    ALTER COLUMN status SET DEFAULT 'APPLIED';

-- 4) Now we can drop the enum type (only if it exists)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'application_status') THEN
DROP TYPE application_status;
END IF;
END $$;
