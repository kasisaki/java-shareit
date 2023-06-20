DROP ALL OBJECTS DELETE FILES;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS item_requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description  VARCHAR(1000) NOT NULL,
    requestor_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    created      TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(50)   NOT NULL,
    description  VARCHAR(1000) NOT NULL,
    is_available BOOLEAN,
    owner_id     BIGINT        NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    request_id   BIGINT        REFERENCES item_requests (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS bookings
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date   timestamp   NOT NULL,
    end_date     timestamp   NOT NULL,
    item_id      BIGINT      NOT NULL REFERENCES items (id) ON DELETE CASCADE,
    requestor_id BIGINT      NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    request_id   BIGINT      REFERENCES item_requests (id) ON DELETE SET NULL,
    status       VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text          varchar(1000) NOT NULL,
    commentAuthor BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    item          BIGINT REFERENCES items (id) ON DELETE CASCADE,
    time_created  TIMESTAMP     not null
);
