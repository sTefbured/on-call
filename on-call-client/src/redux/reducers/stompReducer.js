import {Stomp} from "@stomp/stompjs";
import SockJs from "sockjs-client";

const WEBSOCKET_ADDRESS = "http://localhost:8080/ws";

export const SET_STOMP_CLIENT = "SET_STOMP_CLIENT";

let initialState = {
    stompClient: null
}

const stompReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_STOMP_CLIENT: {
            return setStompClientAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const setStompClientAction = (state, action) => ({
    ...state,
    stompClient: action.stompClient
});

export const setStompClient = (stompClient) => ({
    type: SET_STOMP_CLIENT,
    stompClient
});

export const initializeStompClient = () => (dispatch) => {
    const stompClient = Stomp.over(() => {
        return new SockJs(WEBSOCKET_ADDRESS);
    });

    stompClient.debug = () => {};
    stompClient.connect({}, () => {
        dispatch(setStompClient(stompClient));
    });
}

export default stompReducer;