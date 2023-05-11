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

CREATE TABLE IF NOT EXISTS item_request (
                                        item_request_id   BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                        description       VARCHAR(255)    NOT NULL,
                                        user_id           BIGINT 		  REFERENCES users (user_id) ON DELETE CASCADE,
                                        created           TIMESTAMP WITHOUT TIME ZONE       NOT NULL
);

CREATE TABLE IF NOT EXISTS item_answer (
                                            item_answer_id   BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                            item_request_id  BIGINT          REFERENCES item_request (item_request_id) ON DELETE CASCADE,
                                            item_id          BIGINT          REFERENCES items (item_id) ON DELETE CASCADE
);


