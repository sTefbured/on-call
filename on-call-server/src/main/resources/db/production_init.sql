/* Insert users */
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'admin',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'admin@mail.com', 'Jack', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);

/* Insert permissions */
insert into permissions(name)
values ('register:batch'),
       ('users:list'),
       ('users:show'),
       ('database:runQuery');

/* Insert roles */
insert into roles(name)
values ('Administrator'),
       ('User'),
       ('Group owner');

/* Insert roles_permissions */
insert into roles_permissions(role_id, permission_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 2),
       (2, 3),
       (3, 1);

/* Insert users_roles */
insert into users_roles(role_id, user_id)
values (1, 1);

/* Insert user_groups */
insert into user_groups(id, id_tag, name, description, creation_date, creator_id, owner_id, parent_group_id)
values (0, '', 'On-call group', 'The main group with default permissions and roles.',
        '0001-01-01 00:00:00', 0, 0, null);