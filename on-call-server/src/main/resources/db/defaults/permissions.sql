insert into permissions(id, name, description)
values (12321324655323, 'database:access', 'Allows to run direct queries to database via database tool'),

       (73235634574521, 'user:register', 'Allows to register new users'),
       (76543265436431, 'user:publicInfoView', 'Allows to view users public information'),
       (53135653436991, 'user:privateInfoView', 'Allows to view users private information'),
       (42134563414523, 'user:edit', 'Allows to edit users information'),
       (63442674246236, 'user:ban', 'Allows to ban/unban unprivileged users'),
       (56852268965535, 'moderator:ban', 'Allows to ban/unban moderators'),

       (22965454094518, 'group:publicInfoView', 'Allows to view group public information'),
       (73468932467853, 'group:memberView', 'Allows to view group information for members'),
       (62535625772466, 'group:adminView', 'Allows to view full group information'),
       (86423664324692, 'group:create', 'Allows to create groups/subgroups'),
       (10458224573824, 'group:edit', 'Allows to edit groups'),
       (32465425653256, 'group:delete', 'Allows to delete groups'),
       (61119559324084, 'group:addMember', 'Allows to add group members'),
       (51923288243959, 'group:createJoinRequest', 'Allows to create join requests'),
       (34129460043004, 'groupChat:create', 'Allows to create group chats'),

       (84357654276542, 'userGrant:create', 'Allows to add grants to users'),
       (22355436897676, 'userGrant:view', 'Allows to view grants'),
       (88632678876478, 'userGrant:delete', 'Allows to delete grants'),

       (12670987654342, 'scheduleRecord:create', 'Allows to create schedule records for groups and for other users'),
       (53255425315633, 'scheduleRecord:view', 'Allows to view schedule records for groups'),
       (99326954523576, 'scheduleRecord:edit', 'Allows to edit schedule records for groups'),
       (33325124635736, 'scheduleRecord:delete', 'Allows to delete schedule records for groups'),

       (12344223441245, 'chat:create', 'Allows to create chats'),
       (23512122457000, 'chat:view', 'Allows to view chat'),
       (14668732800753, 'chat:edit', 'Allows to edit chat'),
       (22341245870053, 'chat:delete', 'Allows to delete chat'),
       (66367600965300, 'chat:addMember', 'Allows to add chat members'),
       (22136787520054, 'chat:removeMember', 'Allows to remove chat members'),
       (22456211589043, 'message:send', 'Allows to send messages'),
       (10003134992874, 'message:view', 'Allows to view messages');