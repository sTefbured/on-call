import {scheduleApi} from "../../api/scheduleApi";

export const SET_EVENT_DATE_TIME = "SET_EVENT_DATE_TIME";
export const SET_NAME = "SET_NAME";
export const SET_DESCRIPTION = "SET_DESCRIPTION";
export const SET_USER = "SET_USER";
export const SET_SCHEDULE_RECORD = "SET_SCHEDULE_RECORD";
export const SET_SCHEDULE_RECORDS = "SET_SCHEDULE_RECORDS";
export const SET_FROM = "SET_FROM";
export const SET_TO = "SET_TO";

const findFromDate = () => {
    let date = new Date();
    while (date.getDay() !== 1) {
        date.setDate(date.getDate() - 1);
    }
    date.setHours(0, -date.getTimezoneOffset(), 0, 0);
    return date.toISOString().replace('Z', '');
}

let initialState = {
    scheduleRecords: [],
    scheduleRecord: {
        id: null,
        eventDateTime: '',
        name: '',
        description: '',
        user: null,
        group: null
    },
    from: findFromDate(),
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
        case SET_SCHEDULE_RECORD: {
            return setScheduleRecordAction(state, action);
        }
        case SET_SCHEDULE_RECORDS: {
            return setScheduleRecordsAction(state, action);
        }
        case SET_FROM: {
            return setFromAction(state, action);
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

const setUserAction = (state, action) => ({
    ...state,
    scheduleRecord: {
        ...state.scheduleRecord,
        user: action.user
    }
});

const setScheduleRecordAction = (state, action) => ({
    ...state,
    scheduleRecord: action.scheduleRecord
})

const setScheduleRecordsAction = (state, action) => ({
    ...state,
    scheduleRecords: action.scheduleRecords
});

const setFromAction = (state, action) => ({
    ...state,
    from: action.from
});

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

export const setScheduleRecord = (scheduleRecord) => ({
    type: SET_SCHEDULE_RECORD,
    scheduleRecord
});

export const setScheduleRecords = (scheduleRecords) => ({
    type: SET_SCHEDULE_RECORDS,
    scheduleRecords
});

export const setFrom = (from) => ({
    type: SET_FROM,
    from
})

export const createScheduleRecord = (scheduleRecord, userId, from) => (dispatch) => {
    let to = new Date(from);
    to.setDate(to.getDate() + 6);
    to.setHours(23, 59 - to.getTimezoneOffset(), 59, 999);
    to = to.toISOString().replace('Z', '');
    scheduleApi.addScheduleRecord(scheduleRecord)
        .then(response => {
            if (response.status === 201 && userId && from && to) {
                dispatch(getUserScheduleRecords(userId, from, to))
            }
        });
}

export const getScheduleRecord = (recordId) => (dispatch) => {
    scheduleApi.getScheduleRecord(recordId)
        .then(response => {
            dispatch(setScheduleRecord(response.data))
        });
}

export const getUserScheduleRecords = (userId, from) => (dispatch) => {
    let to = new Date(from);
    to.setDate(to.getDate() + 6);
    to.setHours(23, 59 - to.getTimezoneOffset(), 59, 999);
    to = to.toISOString().replace('Z', '')
    scheduleApi.getUserScheduleRecords(userId, from, to)
        .then(response => {
            dispatch(setScheduleRecords(response.data));
        })
}

export default scheduleReducer;