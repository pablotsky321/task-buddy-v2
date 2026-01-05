create table users(
    id uuid default gen_random_uuid() primary key,
    complete_name varchar(255),
    email varchar(255) unique,
    password text
);

create table task(
    id uuid default gen_random_uuid() primary key,
    title varchar(50),
    description text,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    user_id uuid references users(id)
);

