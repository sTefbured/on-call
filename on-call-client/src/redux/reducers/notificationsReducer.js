import {notificationApi} from "../../api/notificationApi";

export const ADD_NOTIFICATIONS = "ADD_NOTIFICATIONS";
export const CLEAR_NOTIFICATIONS = "CLEAR_NOTIFICATIONS";

let initialState = {
    notifications: [],
}

const notificationsReducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_NOTIFICATIONS: {
            return addNotificationsAction(state, action);
        }
        case CLEAR_NOTIFICATIONS: {
            return clearNotificationsAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const addNotificationsAction = (state, action) => {
    return {
        ...state,
        notifications: action.isAppend
            ? [...state.notifications, ...action.notifications]
            : [...action.notifications, ...state.notifications]
    }
}

const clearNotificationsAction = (state) => ({
    ...state,
    notifications: []
});

export const addNotifications = (notifications, isAppend) => ({
    type: ADD_NOTIFICATIONS,
    notifications,
    isAppend
});

export const clearNotifications = () => ({
    type: CLEAR_NOTIFICATIONS
});

export const loadNotifications = (userId) => (dispatch) => {
    notificationApi.getAllNotifications(userId)
        .then(response => {
            dispatch(addNotifications(response.data, true));
        });
}

export const readAllNotifications = (notifications) => (dispatch) => {
    dispatch(clearNotifications());
    notifications.forEach(notification => {
        if (notification.isActive) {
            notificationApi.readNotification(notification)
                .then(response => {
                    dispatch(addNotifications([response.data], true));
                });
        } else {
            dispatch(addNotifications([notification], true));
        }
    })
}

export default notificationsReducer;