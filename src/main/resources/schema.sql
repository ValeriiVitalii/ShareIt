CREATE TABLE IF NOT EXISTS users (
                                     user_id     BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     name        VARCHAR(255)    NOT NULL,
                                     email       VARCHAR(512)    NOT NULL,
                                     CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);


CREATE TABLE IF NOT EXISTS items (
                                     item_id     BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     name        VARCHAR(255)    NOT NULL,
                                     description VARCHAR(512)    NOT NULL,
                                     available   BOOLEAN         NOT NULL,
                                     owner       BIGINT 		 REFERENCES users (user_id) ON DELETE CASCADE


);

CREATE TABLE IF NOT EXISTS comments (
                                        comment_id   BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                        text         VARCHAR(255)    NOT NULL,
                                        item_id      BIGINT          REFERENCES items (item_id) ON DELETE CASCADE,
                                        user_id      BIGINT          REFERENCES users (user_id) ON DELETE CASCADE

);

CREATE TYPE booking_status AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS bookings (
                                        booking_id   BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                        start_booking TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
                                        end_booking   TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
                                        item_id      BIGINT          REFERENCES items (item_id) ON DELETE CASCADE,
                                        user_id      BIGINT 		    REFERENCES users (user_id) ON DELETE CASCADE,
                                        status       booking_status  NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
                                        request_id   BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                        text         VARCHAR(255)    NOT NULL,
                                        user_id      BIGINT 		    REFERENCES users (user_id) ON DELETE CASCADE
);

DELETE FROM users where user_id > 0;
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;

DELETE FROM items where item_id > 0;
ALTER TABLE items ALTER COLUMN item_id RESTART WITH 1;

DELETE FROM comments where comment_id > 0;
ALTER TABLE comments ALTER COLUMN comment_id RESTART WITH 1;

DELETE FROM bookings where booking_id > 0;
ALTER TABLE bookings ALTER COLUMN booking_id RESTART WITH 1;

