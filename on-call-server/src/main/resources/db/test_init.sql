/* Insert users */
insert into users(id, is_enabled, username, password, email, first_name,
                  last_name, birth_date, registration_date, last_visit,
                  password_expiration_date, user_expiration_date, is_banned)
values (0, true, 'SYSTEM',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0elOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'system@system.com', 'SYSTEM', 'SYSTEM', '2000-11-11 00:00:00',
        '1000-01-01 00:00:00', null,
        null, null, false),
       (1, true, 'admin',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'admin@mail.com', 'Jack', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false),
       (2, true, 'user1',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user1@mail.com', 'Paul', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false),
       (3, true, 'user2',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user2@mail.com', 'John', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false),
       (4, true, 'user3',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user3@mail.com', 'Walter', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false),
       (5, true, 'user4',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user4@mail.com', 'Allan', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false),
       (6, true, 'user5',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user5@mail.com', 'Ken', 'Jackson', '2000-11-11 00:00:00',
        '2022-02-05 00:00:00', null,
        null, null, false),
       (7, true, 'user6',
        '$2a$12$U5DWxLVMEwdaRlX2fsmu0eLOrZgyH9P6KZs.dElLWqd1/.VsNBX8W', /* "adminpass" */
        'user6@mail.com', 'Leon', 'Jackson', '2000-11-11 00:00:00',
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
values (1, 1),
       (2, 2),
       (3, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7);

/* Insert user_groups */
insert into user_groups(id, id_tag, name, description, creation_date, creator_id, owner_id, parent_group_id)
values (0, '', 'On-call group', 'The main group with default permissions and roles.',
        '0001-01-01 00:00:00', 0, 0, null),
       (1, 'a1', '1 level group 1', 'Test group 1.',
        '2022-02-21 21:00:00', 1, 1, 0),
       (2, 'a2', '1 level group 2', 'Test group 2.',
        '2022-02-21 21:00:00', 1, 1, 0),
       (3, 'a3', '1 level group 3', 'Test group 3.',
        '2022-02-21 21:00:00', 1, 1, 0),
       (4, 'b1', '2 level group 1', 'Test group 4.',
        '2022-02-21 21:00:00', 1, 1, 1),
       (5, 'b2', '2 level group 2', 'Test group 5',
        '2022-02-21 21:00:00', 1, 1, 1),
       (6, 'b3', '2 level group 3', 'Test group 6.',
        '2022-02-21 21:00:00', 1, 1, 2),
       (7, 'b4', '2 level group 4', 'Test group 7.',
        '2022-02-21 21:00:00', 1, 1, 3),
       (8, 'ccc', '2 level group 5', 'Test group 8.',
        '2022-02-21 21:00:00', 1, 1, 3),
       (9, 'ccc', '3 level group 1', 'Test group 9.',
        '2022-02-21 21:00:00', 1, 1, 4),
       (10, 'ccc', '3 level group 2', 'Test group 10.',
        '2022-02-21 21:00:00', 1, 1, 5),
       (11, 'c3', '3 level group 3', 'Test group 11.',
        '2022-02-21 21:00:00', 1, 1, 6),
       (12, 'c4', '3 level group 4', 'Test group 12',
        '2022-02-21 21:00:00', 1, 1, 6);

/* Insert user_groups_members */
insert into user_groups_members(group_id, member_id)
values (9, 3),
       (9, 4),
       (4, 6);