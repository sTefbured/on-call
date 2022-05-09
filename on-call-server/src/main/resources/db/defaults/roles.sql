insert into roles(id, description, name, role_type_id)
values (784532567455345, 'Administrator of the website', 'On-call administrator', 75454522355345),
       (345765242562356, 'A regular user of the website', 'On-call user', 75454522355345),
       (335652845749234, 'Administrator of the group', 'Group administrator', 42353246553234),
       (444562356788634, 'Regular group member', 'Group member', 42353246553234),
       (644563246436574, 'Administrator of the chat', 'Chat administrator', 44236577853424),
       (933456323690086, 'Regular chat member', 'Chat member', 44236577853424);



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
       (784532567455345, 33325124635736), /* scheduleRecord:delete */
       (784532567455345, 12344223441245), /* chat:create */
       (784532567455345, 14668732800753), /* chat:edit */
       (784532567455345, 22341245870053), /* chat:delete */
       (784532567455345, 34129460043004); /* groupChat:create */

/* default role-permission connections for 'On-call user' */
insert into roles_permissions (role_id, permission_id)
values (345765242562356, 76543265436431), /* user:publicInfoView */
       (345765242562356, 12344223441245); /* chat:create */

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
       (335652845749234, 33325124635736), /* scheduleRecord:delete */
       (335652845749234, 34129460043004); /* groupChat:create */

/* default role-permission connections for 'Group user' */
insert into roles_permissions (role_id, permission_id)
values (444562356788634, 73468932467853), /* group:memberView */
       (444562356788634, 53255425315633); /* scheduleRecord:view */

/* default role-permission connections for 'Chat administrator' */
insert into roles_permissions (role_id, permission_id)
values (644563246436574, 23512122457000), /* chat:view */
       (644563246436574, 14668732800753), /* chat:edit */
       (644563246436574, 22341245870053), /* chat:delete */
       (644563246436574, 66367600965300), /* chat:addMember */
       (644563246436574, 22136787520054), /* chat:removeMember */
       (644563246436574, 22456211589043), /* message:send */
       (644563246436574, 10003134992874); /* message:view */

/* default role-permission connections for 'Chat member' */
insert into roles_permissions (role_id, permission_id)
values (933456323690086, 23512122457000), /* chat:view */
       (933456323690086, 66367600965300), /* chat:addMember */
       (933456323690086, 22456211589043), /* message:send */
       (933456323690086, 10003134992874); /* message:view */
