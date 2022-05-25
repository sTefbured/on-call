export const hasUserPermissionForGroup = (user, permission, groupId) => {
    return user.grants.some(grant => {
        return grant.group?.id === groupId && grant.permissions.some(p=> p.id === permission);
    });
}