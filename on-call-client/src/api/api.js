import axios from "axios";

const SERVER_URL = "http://localhost:8080/";

const axiosInstance = axios.create({
    baseURL: SERVER_URL + "api/v1/",
    withCredentials: true
})

export const userApi = {
    getUserById(id) {
        return axiosInstance.get("user/" + id)
    },

    getAllUsers(page, pageSize) {
        return axiosInstance.get("user/all", {
            params: {
                page: page,
                pageSize: pageSize
            },
            withCredentials: true
        })
    },

    register(user, avatar) {
        let formData = new FormData();
        formData.append("avatar", avatar);
        formData.append("user", new Blob([JSON.stringify(user)], {type: "application/json"}));
        return axiosInstance.post("user/", formData, {
            headers: {
                "content-type": "multipart/form-data"
            }
        });
    },

    login(username, password) {
        let userCredentials = {
            username,
            password
        }
        return axiosInstance.post("user/login", userCredentials);
    },

    me() {
        return axiosInstance.get("user/me");
    },

    logout() {
        return axiosInstance.post("user/logout");
    }
}