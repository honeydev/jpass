create sequence user_entity_seq start with 1 increment by 50;
create sequence role_entity_seq start with 1 increment by 50;
create sequence privilege_entity_seq start with 1 increment by 50;

create table user_entity (
    id integer not null,
    email varchar(100) not null,
    password_hash varchar(255) not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint pk_user_entity primary key (id),
    constraint uk_user_entity_email unique (email)
);

create table role_entity (
    id bigint not null,
    name varchar(255),
    constraint pk_role_entity primary key (id)
);

create table privilege_entity (
    id bigint not null,
    name varchar(255),
    constraint pk_privilege_entity primary key (id)
);

create table users_roles (
    user_id integer not null,
    role_id bigint not null,
    constraint pk_users_roles primary key (user_id, role_id),
    constraint fk_users_roles_user foreign key (user_id) references user_entity (id),
    constraint fk_users_roles_role foreign key (role_id) references role_entity (id)
);

create table roles_privileges (
    role_id bigint not null,
    privilege_id bigint not null,
    constraint pk_roles_privileges primary key (role_id, privilege_id),
    constraint fk_roles_privileges_role foreign key (role_id) references role_entity (id),
    constraint fk_roles_privileges_privilege foreign key (privilege_id) references privilege_entity (id)
);
