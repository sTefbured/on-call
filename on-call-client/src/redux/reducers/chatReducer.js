import {chatApi} from "../../api/chatApi";

export const SET_CHAT = "SET_CHAT";
export const SET_CHATS = "SET_CHATS";
export const ADD_MESSAGES = "ADD_MESSAGES";
export const CLEAR_MESSAGES = "CLEAR_MESSAGES";
export const SET_CURRENT_PAGE = "SET_CURRENT_PAGE";

let initialState = {
    messages: [],
    chat: null,
    chats: [],
    currentPage: 0,
    defaultPageSize: 20
}

const chatReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_CHAT: {
            return setChatAction(state, action);
        }
        case SET_CHATS: {
            return setChatsAction(state, action);
        }
        case ADD_MESSAGES: {
            return addMessagesAction(state, action);
        }
        case CLEAR_MESSAGES: {
            return clearMessagesAction(state);
        }
        case SET_CURRENT_PAGE: {
            return setCurrentPageAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const setChatAction = (state, action) => ({
    ...state,
    chat: action.chat
});

const setChatsAction = (state, action) => ({
    ...state,
    chats: action.chats
});

const addMessagesAction = (state, action) => ({
    ...state,
    messages: action.isAppend
        ? [...state.messages, ...action.messages]
        : [...action.messages, ...state.messages]
});

const clearMessagesAction = (state) => ({
    ...state,
    messages: []
});

const setCurrentPageAction = (state, action) => ({
    ...state,
    currentPage: action.currentPage
});

export const setChat = (chat) => ({
    type: SET_CHAT,
    chat
});

export const setChats = (chats) => ({
    type: SET_CHATS,
    chats
});

export const addMessages = (messages, isAppend) => ({
    type: ADD_MESSAGES,
    messages,
    isAppend
});

export const clearMessages = () => ({
    type: CLEAR_MESSAGES
});

export const setCurrentPage = (currentPage) => ({
    type: SET_CURRENT_PAGE,
    currentPage
});

export const requestChatInfo = (chatId) => (dispatch) => {
    return chatApi.getChatById(chatId)
        .then(response => {
            dispatch(setChat(response.data));
            loadMessages(chatId, initialState.currentPage, initialState.defaultPageSize)(dispatch);
        });
}

export const loadMessages = (chatId, page, pageSize) => (dispatch) => {
    return chatApi.getAllMessages(chatId, page, pageSize)
        .then(response => {
            if (response.status === 200) {
                dispatch(addMessages(response.data.reverse(), false));
            }
        });
}

export const queryChatsForUser = (userId, page, pageSize) => (dispatch) => {
    return chatApi.getAllChatsForUser(userId, page, pageSize)
        .then(response => {
            dispatch(setChats(response.data));
        });
}

export const findOrCreateChat = (targetUserId) => (dispatch) => {
    return chatApi.findOrCreateChat(targetUserId)
        .then(response => {
            dispatch(setChat(response.data));
        });
}

export default chatReducer;