import {userApi} from "../../api/api";

export const SET_USERNAME = "SET_USERNAME";
export const SET_PASSWORD = "SET_PASSWORD";
export const SET_AUTHORIZED_USER_DATA = "SET_AUTHORIZED_USER_DATA";

let initialState = {
    username: "",
    password: "",
    user: null,
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
        case SET_AUTHORIZED_USER_DATA: {
            return setAuthorizedUserDataAction(state, action);
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

const setAuthorizedUserDataAction = (state, action) => {
    return {
        ...state,
        user: action.user,
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

export const setAuthorizedUserData = (isAuthorized, user) => ({
    type: SET_AUTHORIZED_USER_DATA,
    isAuthorized,
    user
});

export const login = (username, password) => (dispatch) => {
    userApi.login(username, password)
        .then(response => {
            let isSuccessful = response.status === 200;
            if (isSuccessful) {
                dispatch(setUsername(""));
                dispatch(setPassword(""));
            }
            me()(dispatch);
        })
        .catch(() => dispatch(setAuthorizedUserData(false, null)))
}

export const me = () => (dispatch) => {
    userApi.me()
        .then(response => {
            dispatch(setAuthorizedUserData(response.status === 200, response.data || null))
        })
        .catch(error => {
            if (error.response.status === 401) {
                dispatch(setAuthorizedUserData(false, null));
            }
        });
}

export const logout = () => (dispatch) => {
    userApi.logout()
        .then(() => {
            dispatch(setAuthorizedUserData(false, null));
        })
}

export default authReducer;