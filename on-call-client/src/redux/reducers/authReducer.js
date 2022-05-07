import {userApi} from "../../api/api";

export const SET_USERNAME = "SET_USERNAME";
export const SET_PASSWORD = "SET_PASSWORD";
export const SET_IS_AUTHORIZED = "SET_IS_AUTHORIZED";

let initialState = {
    username: "",
    password: "",
    isAuthorized: false
}

const authReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_USERNAME: {
            return setUsernameAction(state, action);
        }
        case SET_PASSWORD: {
            return setPasswordAction(state, action);
        }
        case SET_IS_AUTHORIZED: {
            return setIsAuthorizedAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const setUsernameAction = (state, action) => {
    return {
        ...state,
        username: action.username
    }
}

const setPasswordAction = (state, action) => {
    return {
        ...state,
        password: action.password
    }
}

const setIsAuthorizedAction = (state, action) => {
    return {
        ...state,
        isAuthorized: action.isAuthorized
    }
}

export const setUsername = (username) => ({
    type: SET_USERNAME,
    username
});

export const setPassword = (password) => ({
    type: SET_PASSWORD,
    password
});

export const setIsAuthorized = (isAuthorized) => ({
    type: SET_IS_AUTHORIZED,
    isAuthorized
});

export const login = (username, password) => (dispatch) => {
    userApi.login(username, password)
        .then(response => {
            let isSuccessful = response.status === 200;
            if (isSuccessful) {
                dispatch(setUsername(""));
                dispatch(setPassword(""));
            }
            dispatch(setIsAuthorized(isSuccessful));
        })
        .catch(() => dispatch(setIsAuthorized(false)))
}

export const me = () => (dispatch) => {
    userApi.me()
        .then(response => {
            dispatch(setIsAuthorized(response.status === 200))
        })
        .catch(error => {
            if (error.response.status === 401) {
                dispatch(setIsAuthorized(false));
            }
        });
}

export const logout = () => (dispatch) => {
    userApi.logout()
        .then(() => {
            dispatch(setIsAuthorized(false));
        })
}

export default authReducer;