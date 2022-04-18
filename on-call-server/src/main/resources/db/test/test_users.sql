insert into users(id, birth_date, email, first_name, is_banned, is_enabled, last_name,
                  last_visit_date_time, password, password_expiration_date, registration_date_time, username)
values (7433467721245657, to_date('09-11-2000', 'dd-MM-yyyy'), 'on-call@gmail.com', 'Stepan',
        false, true, 'Kotikov', to_date('17-04-2022 10:15:45', 'dd-mm-yyyy hh:mi:ss'),
        '$2a$12$B8NFhR1Dql0vLuE3uGn31Olj4zdMq6axHqbKOMYTn3/KpFdY2pfuS', null,        /* 1Password */
        to_date('17-04-2022 10:15:45', 'dd-mm-yyyy hh:mi:ss'), 'sTefbured-admin');

insert into users(id, birth_date, email, first_name, is_banned, is_enabled, last_name,
                  last_visit_date_time, password, password_expiration_date, registration_date_time, username)
values (1233467754445657, to_date('19-11-2001', 'dd-MM-yyyy'), 'user@gmail.com', 'Gennadiy',
        false, true, 'Johnson', to_date('17-04-2022 10:15:45', 'dd-mm-yyyy hh:mi:ss'),
        '$2a$12$B8NFhR1Dql0vLuE3uGn31Olj4zdMq6axHqbKOMYTn3/KpFdY2pfuS', null,        /* 1Password */
        to_date('17-04-2022 10:15:45', 'dd-mm-yyyy hh:mi:ss'), 'sTefbured-user');