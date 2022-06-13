export const ADD_MEMBER_PERMISSION = 61119559324084;
export const GROUP_MEMBER_VIEW_PERMISSION = 73468932467853;
export const GROUP_CHAT_CREATE_PERMISSION = 34129460043004;

export const hasUserPermissionForGroup = (user, permission, groupId) => {
    return user.grants.some(grant => {
        return grant.group?.id === groupId && grant.permissions.some(p=> p.id === permission);
    });
}