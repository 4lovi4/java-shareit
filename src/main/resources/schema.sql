CREATE TABLE IF NOT EXISTS users (id integer, name varchar(255), email varchar(64));
CREATE TABLE IF NOT EXISTS items (id integer, name varchar(255), description varchar(1024), available boolean, owner_id bigint, request bigint);