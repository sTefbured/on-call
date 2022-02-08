/* Insert users */
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'admin',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'admin@mail.com', 'Jack', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'user1',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user1@mail.com', 'Paul', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'user2',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user2@mail.com', 'John', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'user3',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user3@mail.com', 'Walter', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'user4',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user4@mail.com', 'Allan', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'user5',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user5@mail.com', 'Ken', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);
insert into users(is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (true, 'user6',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user6@mail.com', 'Leon', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false);

/* Insert permissions */
insert into permissions(name)
values ('register:batch'),
       ('users:list'),
       ('users:show');

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
       (2, 2),
       (2, 3),
       (3, 1);

/* Insert users_roles */
insert into users_roles(role_id, user_id)
values (1, 1),
       (2, 2),
       (3, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7);