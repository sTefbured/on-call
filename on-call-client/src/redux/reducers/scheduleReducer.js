import {scheduleApi} from "../../api/scheduleApi";

export const SET_EVENT_DATE_TIME = "SET_EVENT_DATE_TIME";
export const SET_NAME = "SET_NAME";
export const SET_DESCRIPTION = "SET_DESCRIPTION";
export const SET_USER = "SET_USER";

let initialState = {
    scheduleRecord: {
        id: null,
        eventDateTime: '',
        name: '',
        description: '',
        user: null,
        group: null
    }
}

const scheduleReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_EVENT_DATE_TIME: {
            return setEventDateTimeAction(state, action);
        }
        case SET_NAME: {
            return setNameAction(state, action);
        }
        case SET_DESCRIPTION: {
            return setDescriptionAction(state, action);
        }
        case SET_USER: {
            return setUserAction(state, action);
        }
        default: {
            return state
        }
    }
}

const setEventDateTimeAction = (state, action) => ({
    ...state,
    scheduleRecord: {
        ...state.scheduleRecord,
        eventDateTime: action.eventDateTime
    }
});

const setNameAction = (state, action) => ({
    ...state,
    scheduleRecord: {
        ...state.scheduleRecord,
        name: action.name
    }
});

const setDescriptionAction = (state, action) => ({
    ...state,
    scheduleRecord: {
        ...state.scheduleRecord,
        description: action.description
    }
});

const setUserAction = (state, action) => {
    return {
        ...state,
        scheduleRecord: {
            ...state.scheduleRecord,
            user: action.user
        }
    }
};

export const setEventDateTime = (eventDateTime) => ({
    type: SET_EVENT_DATE_TIME,
    eventDateTime
});

export const setName = (name) => ({
    type: SET_NAME,
    name
});

export const setDescription = (description) => ({
    type: SET_DESCRIPTION,
    description
});

export const setUser = (userId) => ({
    type: SET_USER,
    user: {
        id: userId
    }
});

export const addScheduleRecord = (scheduleRecord) => (dispatch) => {
    scheduleApi.addScheduleRecord(scheduleRecord)
        .then();
}

export default scheduleReducer;