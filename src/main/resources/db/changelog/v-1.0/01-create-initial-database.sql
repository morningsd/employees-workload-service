create table department (
    id uuid not null,
    description varchar(1024) not null,
    name varchar(255) not null,
    primary key (id)
);

create table project (
    id uuid not null,
    description varchar(1024) not null,
    name varchar(255) not null,
    primary key (id)
);

create table user_project (
    project_id uuid not null,
    user_id uuid not null,
    position_end_date date not null,
    position_start_date date not null,
    working_hours int4 not null,
    primary key (project_id, user_id)
);

create table usr (
    id uuid not null,
    email varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    password varchar(255) not null,
    department_id uuid,
    primary key (id)
);

alter table if exists department
    add constraint UK_1t68827l97cwyxo9r1u6t4p7d unique (name);

alter table if exists project
    add constraint UK_3k75vvu7mevyvvb5may5lj8k7 unique (name);

alter table if exists usr
    add constraint UK_g9l96r670qkidthshajdtxrqf unique (email);

alter table if exists user_project
    add constraint FKocfkr6u2yh3w1qmybs8vxuv1c foreign key (project_id) references project;

alter table if exists user_project
    add constraint FK5n9393t40e47m9srydudsq9ew foreign key (user_id) references usr;

alter table if exists usr
    add constraint FKpxhk0d0wmwt6budg7q4qsic5n foreign key (department_id) references department;
