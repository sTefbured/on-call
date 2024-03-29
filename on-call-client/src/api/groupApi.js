import {axiosInstance} from "./api";

export const groupApi = {
    createGroup(group) {
        return axiosInstance.post("/group", group);
    },

    getUserByIdTag(idTag) {
        return axiosInstance.get("group/seq/" + idTag)
    },

    getGroupMembers(groupId, page, pageSize) {
        return axiosInstance.get("group/members", {
            params: {
                groupId,
                page,
                pageSize
            }
        });
    },

    getFirstLevelGroups(page, pageSize) {
        return axiosInstance.get("group/all", {
            params: {
                page: page,
                pageSize: pageSize
            },
            withCredentials: true
        })
    },

    getGroupByTagSequence(tagSequence) {
        return axiosInstance.get('/group/seq/' + tagSequence);
    },

    createJoinRequest(joinRequest) {
        return axiosInstance.post('/group/join', joinRequest);
    },

    getAllJoinRequests(groupId, page, pageSize) {
        return axiosInstance.get("/group/" + groupId + "/join", {
            params: {
                page,
                pageSize
            }
        });
    },

    approveJoinRequest(request) {
        return axiosInstance.post("/group/join/approve", request);
    },

    getGroupById(groupId) {
        return axiosInstance.get("group/" + groupId);
    },

    getAllGroupsForUser(userId, page, pageSize) {
        return axiosInstance.get("group/all/user/" + userId, {
            params: {
                page: page,
                pageSize: pageSize
            }
        });
    }
}