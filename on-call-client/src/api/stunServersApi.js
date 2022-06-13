import {axiosInstance} from "./api";

const SERVER_LINK = "https://raw.githubusercontent.com/pradt2/always-online-stun/master/valid_hosts.txt";

export const stunServersApi = {
    getAvailableStunServers() {
        return axiosInstance.get(SERVER_LINK, {withCredentials: false});
    }
}