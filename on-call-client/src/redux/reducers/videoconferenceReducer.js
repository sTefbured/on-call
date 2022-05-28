import {videoconferenceApi} from "../../api/videoconferenceApi";
import {stunServersApi} from "../../api/stunServersApi";

export const SET_CONFERENCE_ROOMS = "SET_CONFERENCE_ROOMS";
export const ADD_CONFERENCE_ROOM = "ADD_CONFERENCE_ROOM";
export const SET_CONFERENCE_ROOM = "SET_CONFERENCE_ROOM";
export const SET_PAGE = "SET_PAGE";
export const SET_IS_AUTHORIZED = "SET_IS_AUTHORIZED";
export const SET_ACCESS_CODE = "SET_ACCESS_CODE";

let initialState = {
    conferenceRooms: [],
    conferenceRoom: {},
    page: 0,
    pageSize: 20,
    isAuthorized: false,
    accessCode: ""
}

const videoconferenceReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_CONFERENCE_ROOMS: {
            return setConferenceRoomsAction(state, action);
        }
        case SET_CONFERENCE_ROOM: {
            return setConferenceRoomAction(state, action);
        }
        case SET_PAGE: {
            return setPageAction(state, action);
        }
        case SET_IS_AUTHORIZED: {
            return setIsAuthorizedAction(state, action);
        }
        case SET_ACCESS_CODE: {
            return setAccessCodeAction(state, action);
        }
        case ADD_CONFERENCE_ROOM: {
            return addConferenceRoomAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const addConferenceRoomAction = (state, action) => ({
    ...state,
    conferenceRooms: [...state.conferenceRooms, action.conferenceRoom]
});

const setConferenceRoomsAction = (state, action) => ({
    ...state,
    conferenceRooms: action.conferenceRooms
});

const setConferenceRoomAction = (state, action) => ({
    ...state,
    conferenceRoom: action.conferenceRoom
});

const setPageAction = (state, action) => ({
    ...state,
    page: action.page
});

const setIsAuthorizedAction = (state, action) => ({
    ...state,
    isAuthorized: action.isAuthorized
});

const setAccessCodeAction = (state, action) => ({
    ...state,
    accessCode: action.accessCode
});

export const setConferenceRooms = (conferenceRooms) => ({
    type: SET_CONFERENCE_ROOMS,
    conferenceRooms
});

export const setConferenceRoom = (conferenceRoom) => ({
    type: SET_CONFERENCE_ROOM,
    conferenceRoom
});

export const setPage = (page) => ({
    type: SET_PAGE,
    page
});

export const setIsAuthorized = (isAuthorized) => ({
    type: SET_IS_AUTHORIZED,
    isAuthorized
});

export const setAccessCode = (accessCode) => ({
    type: SET_ACCESS_CODE,
    accessCode
});

export const addConferenceRoom = (conferenceRoom) => ({
    type: ADD_CONFERENCE_ROOM,
    conferenceRoom
});

export const getVideoconferenceRoomsForUser = (userId, page, pageSize) => (dispatch) => {
    videoconferenceApi.getVideoconferenceRoomsForUser(userId, page, pageSize)
        .then(response => {
            dispatch(setConferenceRooms(response.data));
        });
}

export const getVideoconferenceRoomById = (roomId, accessCode) => (dispatch) => {
    return videoconferenceApi.getVideoconferenceRoomById(roomId, accessCode)
        .then(response => {
            if (response.status === 200) {
                dispatch(setIsAuthorized(true));
                dispatch(setConferenceRoom(response.data));
            }
        });
}

export const getStunServers = () => (dispatch) => {
    return stunServersApi.getAvailableStunServers()
        .then(result => {
            return result.data.split('\n');
        })
}

export const createVideoconferenceRoom = (videoconferenceRoom) => (dispatch) => {
    return videoconferenceApi.createVideoconferenceRoom(videoconferenceRoom)
        .then(response => {
            dispatch(addConferenceRoom(response.data));
        })
}

export default videoconferenceReducer;