import {axiosInstance} from "./api";

export const videoconferenceApi = {
    getVideoconferenceRoomsForUser(userId, page, pageSize) {
        return axiosInstance.get("videoconference/user/" + userId, {
            params: {
                page: page,
                pageSize: pageSize
            }
        });
    },

    getVideoconferenceRoomById(roomId, accessCode) {
        return axiosInstance.post("videoconference/" + roomId, {
            params: {
                accessCode: accessCode
            }
        });
    },

    createVideoconferenceRoom(videoconferenceRoom) {
        return axiosInstance.post("videoconference", videoconferenceRoom);
    }
}