import {axiosInstance} from "./api";

export const chatApi = {
    createChat(chat) {
        return axiosInstance.post("/chat", chat);
    },

    findOrCreateChat(userId) {
        return axiosInstance.post("/chat/user", {id: userId});
    },

    getAllChatsForUser(userId, page, pageSize) {
        return axiosInstance.get("/chat/user/" + userId, {
            params: {
                page,
                pageSize
            }
        })
    },

    getChatById(chatId) {
        return axiosInstance.get("/chat/" + chatId);
    },

    getAllMessages(chatId, page, pageSize) {
        return axiosInstance.get("/message/all", {
            params: {
                chatId,
                page,
                pageSize
            }
        });
    }
}