/* bntu -> fitr -> 10702418 */
insert into groups(id, creation_date_time, description, id_tag, name, creator_id, parent_group_id)
values (634426544342552, to_date('17-04-2022 10:15:45', 'dd-mm-yyyy hh:mi:ss'), 'description text',
        'bntu', 'Belarusian National Technical University', 7433467721245657, null),
       (735965368008523, to_date('17-04-2022 10:15:45', 'dd-mm-yyyy hh:mi:ss'), 'description text',
        'fitr', 'Faculty of Information Technologies and Robotics',
        7433467721245657, 634426544342552),
       (742357854234574, to_date('17-04-2022 10:15:45', 'dd-mm-yyyy hh:mi:ss'), 'description text',
        '10702418', 'Group 10702418',7433467721245657, 735965368008523);

