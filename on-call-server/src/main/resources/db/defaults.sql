/* default permissions */
insert into permissions(id, name, description)
values (12321324655323, 'database:access', 'Allows to run direct queries to database via database tool'),
       (73235634574521, 'user:register', 'Allows to register new users'),
       (76543265436431, 'user:publicInfoView', 'Allows to view users public information'),
       (53135653436991, 'user:privateInfoView', 'Allows to view users private information'),
       (42134563414523, 'user:edit', 'Allows to edit users information'),
       (63442674246236, 'user:ban', 'Allows to ban/unban unprivileged users'),
       (56852268965535, 'moderator:ban', 'Allows to ban/unban moderators'),
       (73468932467853, 'group:memberView', 'Allows to view basic group information'),
       (62535625772466, 'group:adminView', 'Allows to view full group information'),
       (86423664324692, 'group:create', 'Allows to create groups/subgroups'),
       (10458224573824, 'group:edit', 'Allows to edit groups'),
       (32465425653256, 'group:delete', 'Allows to delete groups'),
       (84357654276542, 'userGrant:create', 'Allows to add grants to users'),
       (22355436897676, 'userGrant:view', 'Allows to view grants'),
       (88632678876478, 'userGrant:delete', 'Allows to delete grants'),
       (12670987654342, 'scheduleRecord:create', 'Allows to create schedule records'),
       (53255425315633, 'scheduleRecord:view', 'Allows to view schedule records'),
       (99326954523576, 'scheduleRecord:edit', 'Allows to edit schedule records'),
       (33325124635736, 'scheduleRecord:delete', 'Allows to delete schedule records');

/* default role types */
insert into role_types(id, name)
values (42353246553234, 'group_roles'),
       (75454522355345, 'global_roles'),
       (44236577853424, 'chat_roles');

/* default roles */
insert into roles(id, description, name, role_type_id)
values (784532567455345, 'Administrator of the website', 'On-call administrator', 75454522355345),
       (345765242562356, 'A regular user of the website', 'On-call user', 75454522355345),
       (335652845749234, 'Administrator of the group', 'Group administrator', 42353246553234),
       (444562356788634, 'Regular group member', 'Group member', 42353246553234);

/* default role-permission connections for 'On-call administrator' */
insert into roles_permissions (role_id, permission_id)
values (784532567455345, 12321324655323), /* database:access */
       (784532567455345, 63442674246236), /* user:ban */
       (784532567455345, 53135653436991), /* user:privateInfoView */
       (784532567455345, 73235634574521), /* user:register */
       (784532567455345, 42134563414523), /* user:edit */
       (784532567455345, 56852268965535), /* moderator:ban */
       (784532567455345, 62535625772466), /* group:adminView */
       (784532567455345, 86423664324692), /* group:create */
       (784532567455345, 10458224573824), /* group:edit */
       (784532567455345, 32465425653256), /* group:delete */
       (784532567455345, 84357654276542), /* userGrant:create */
       (784532567455345, 22355436897676), /* userGrant:view */
       (784532567455345, 88632678876478), /* userGrant:delete */
       (784532567455345, 12670987654342), /* scheduleRecord:create */
       (784532567455345, 53255425315633), /* scheduleRecord:view */
       (784532567455345, 99326954523576), /* scheduleRecord:edit */
       (784532567455345, 33325124635736); /* scheduleRecord:delete */

/* default role-permission connections for 'On-call user' */
insert into roles_permissions (role_id, permission_id)
values (345765242562356, 76543265436431); /* user:publicInfoView */

/* default role-permission connections for 'Group administrator' */
insert into roles_permissions (role_id, permission_id)
values (335652845749234, 62535625772466), /* group:adminView */
       (335652845749234, 86423664324692), /* group:create */
       (335652845749234, 10458224573824), /* group:edit */
       (335652845749234, 32465425653256), /* group:delete */
       (335652845749234, 84357654276542), /* userGrant:create */
       (335652845749234, 22355436897676), /* userGrant:view */
       (335652845749234, 88632678876478), /* userGrant:delete */
       (335652845749234, 12670987654342), /* scheduleRecord:create */
       (335652845749234, 53255425315633), /* scheduleRecord:view */
       (335652845749234, 99326954523576), /* scheduleRecord:edit */
       (335652845749234, 33325124635736); /* scheduleRecord:delete */

/* default role-permission connections for 'Group user' */
insert into roles_permissions (role_id, permission_id)
values (444562356788634, 73468932467853); /* group:memberView */
values (444562356788634, 53255425315633); /* scheduleRecord:view */