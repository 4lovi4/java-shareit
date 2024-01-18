CREATE TABLE IF NOT EXISTS users (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(255),
email varchar(100) UNIQUE);

CREATE TABLE IF NOT EXISTS items (id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name varchar(255),
description varchar(1024), available boolean, owner_id bigint, request bigint,
CONSTRAINT fk_items_to_user FOREIGN KEY(owner_id) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS bookings(id bigint GENERATED ALWAYS AS IDENTITY  PRIMARY KEY, item_id bigint,
start_time timestamp, end_time timestamp, booker_id bigint, status varchar(16));

CREATE TABLE IF NOT EXISTS requests(id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, description varchar(1024),
bigint requester_id, created_time timestamp);