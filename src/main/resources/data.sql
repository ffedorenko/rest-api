CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    date_of_birth DATE,
    address       VARCHAR(255),
    phone_number  VARCHAR(255)
);