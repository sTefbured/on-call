import {userApi} from "../../api/api";

export const SET_USERS = "SET_USERS";
export const SET_CURRENT_PAGE = "SET_CURRENT_PAGE";

let initialState = {
    users: [],
    totalCount: 0,
    currentPage: 0,
    pageSize: 20
}

const usersReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_USERS: {
            return setUsersAction(state, action);
        }
        case SET_CURRENT_PAGE: {
            return setCurrentPageAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const setUsersAction = (state, action) => {
    return {
        ...state,
        users: action.users,
        totalCount: action.totalCount
    }
}

const setCurrentPageAction = (state, action) => {
    return {
        ...state,
        currentPage: action.currentPage
    }
}

export const setUsers = (users, totalCount) => ({
    type: SET_USERS,
    users: users,
    totalCount: totalCount
});

export const setCurrentPage = (currentPage) => ({
    type: SET_CURRENT_PAGE,
    currentPage: currentPage
});

export const getAllUsers = (page, pageSize) => (dispatch) => {
    userApi.getAllUsers(page, pageSize)
        .then(response => {
            dispatch(setUsers(response.data, response.headers['content-range']));
        });
}

export default usersReducer;