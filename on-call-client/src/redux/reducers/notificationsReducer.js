import SockJs from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {notificationApi} from "../../api/notificationApi";

export const ADD_NOTIFICATIONS = "ADD_NOTIFICATIONS";

let initialState = {
    notifications: [],
}

const notificationsReducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_NOTIFICATIONS: {
            return addNotificationsAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const addNotificationsAction = (state, action) => {
    return {
        ...state,
        notifications: [...action.notifications, ...state.notifications]
    }
}

export const addNotifications = (notifications) => ({
    type: ADD_NOTIFICATIONS,
    notifications
});

// export const connectViaWebSocket = (userId) => (dispatch) => {
//     let socket = new SockJs('http://localhost:8080/notification');
//     let stompClient = Stomp.over(socket);
//     stompClient.connect({}, () => {
//         notificationApi.getAllNotifications(userId)
//             .then(response => {
//                 // stompClient.subscribe('/')
//                 dispatch(addNotifications(response.data));
//             });
//     })
// }

export default notificationsReducer;