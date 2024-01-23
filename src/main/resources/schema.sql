create table if not exists users (
    id bigint generated always as identity primary key,
    name varchar(255),
    email varchar(100) unique);

create table if not exists requests(
    id bigint generated always as identity primary key,
    description varchar(1024),
    requester_id bigint,
    created_time timestamp,
    constraint fk_requester_to_user foreign key(requester_id) references users(id) on delete cascade);

create table if not exists items (
    id bigint generated always as identity primary key,
    name varchar(255),
    description varchar(1024),
    available boolean,
    owner_id bigint, request_id bigint,
    constraint fk_items_to_user foreign key(owner_id) references users(id) on delete cascade,
    constraint fk_items_to_request foreign key(request_id) references requests(id) on delete cascade);

create table if not exists bookings(
    id bigint generated always as identity primary key,
    item_id bigint,
    start_time timestamp,
    end_time timestamp,
    booker_id bigint, status varchar(16),
    constraint fk_bookings_to_item foreign key(item_id) references items(id) on delete cascade,
    constraint fk_bookings_to_user foreign key(booker_id) references users(id) on delete cascade);

create table if not exists comments(
    id bigint generated always as identity primary key,
    text varchar(2048),
    user_id bigint,
    item_id bigint,
    created_time timestamp,
    constraint fk_comment_to_item foreign key(item_id) references items(id) on delete cascade,
    constraint fk_comment_to_user foreign key(user_id) references users(id) on delete cascade
)
