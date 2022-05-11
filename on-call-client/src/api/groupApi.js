import {axiosInstance} from "./api";

export const groupApi = {
    getUserByIdTag(idTag) {
        return axiosInstance.get("group/seq/" + idTag)
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
    }
}