import {videoconferenceApi} from "../../api/videoconferenceApi";

export const SET_CONFERENCE_ROOMS = "SET_CONFERENCE_ROOMS";
export const SET_PAGE = "SET_PAGE";

let initialState = {
    conferenceRooms: [],
    page: 0,
    pageSize: 20
}

const videoconferenceReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_CONFERENCE_ROOMS: {
            return setConferenceRoomsAction(state, action);
        }
        case SET_PAGE: {
            return setPageAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const setConferenceRoomsAction = (state, action) => ({
    ...state,
    conferenceRooms: action.conferenceRooms
});

const setPageAction = (state, action) => ({
    ...state,
    page: action.page
});

export const setConferenceRooms = (conferenceRooms) => ({
    type: SET_CONFERENCE_ROOMS,
    conferenceRooms
});

export const setPage = (page) => ({
    type: SET_PAGE,
    page
});

export const getVideoconferenceRoomsForUser = (userId, page, pageSize) => (dispatch) => {
    videoconferenceApi.getVideoconferenceRoomsForUser(userId, page, pageSize)
        .then(response => {
            dispatch(setConferenceRooms(response.data));
        });
}

export default videoconferenceReducer;