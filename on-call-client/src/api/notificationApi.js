import {axiosInstance} from "./api";

export const notificationApi = {
    getAllNotifications(userId) {
        return axiosInstance.get("/notifications/" + userId);
    }
}