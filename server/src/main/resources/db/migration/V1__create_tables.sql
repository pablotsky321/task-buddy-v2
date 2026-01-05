create table users(
    id uuid default gen_random_uuid() primary key,
    complete_name varchar(255),
    email varchar(255) unique not null,
    password text not null
);

create type task_state as enum(
'finished','completed','expired','pending'
);

create table task(
    id uuid default gen_random_uuid() primary key,
    title varchar(50) not null,
    description text,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    finish_at timestamp not null,
    completed_at timestamp,
    state task_state default 'pending'::task_state,
    user_id uuid references users(id) on delete cascade on update cascade
);

create cast ( varchar as task_state ) with inout as implicit;

