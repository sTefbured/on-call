import {userApi} from "../../api/api";

export const SET_USER = "SET_USER"

let initialState = {
    user: null
}

let userProfileReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_USER: {
            return setUserAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const setUserAction = (state, action) => {
    return {
        ...state,
        user: action.user
    }
}

export const setUser = (user) => ({
    type: SET_USER,
    user: user
});

export const getUserById = (id) => (dispatch) => {
    return userApi.getUserById(id)
        .then(response => dispatch(setUser(response.data)))
}

export default userProfileReducer;