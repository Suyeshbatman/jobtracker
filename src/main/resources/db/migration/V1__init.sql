CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TYPE application_status AS ENUM (
    'APPLIED',
    'HR',
    'TECH',
    'OFFER',
    'REJECTED',
    'WITHDRAWN'
);

CREATE TABLE applications (
                              id UUID PRIMARY KEY,
                              user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              company VARCHAR(255) NOT NULL,
                              title VARCHAR(255) NOT NULL,
                              job_url TEXT,
                              location VARCHAR(255),
                              status application_status NOT NULL DEFAULT 'APPLIED',
                              applied_date DATE,
                              notes TEXT,
                              created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE application_status_history (
                                            id UUID PRIMARY KEY,
                                            application_id UUID NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
                                            from_status application_status,
                                            to_status application_status NOT NULL,
                                            changed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                            note TEXT
);
