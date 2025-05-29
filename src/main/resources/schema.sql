-- src/main/resources/schema.sql

CREATE TABLE IF NOT EXISTS users (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    full_name          TEXT    NOT NULL,
    phone              TEXT    NOT NULL UNIQUE,
    email              TEXT    UNIQUE,
    password_hash      TEXT    NOT NULL,
    role               TEXT    NOT NULL,
    address            TEXT    NOT NULL,
    profile_image_base64 TEXT,
    bank_name          TEXT,
    account_number     TEXT,
    created_at         TEXT    DEFAULT (datetime('now'))
);
