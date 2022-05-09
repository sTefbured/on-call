import {groupApi} from "../../api/groupApi";

export const SET_GROUPS = "SET_GROUPS";
export const SET_CURRENT_PAGE = "SET_CURRENT_PAGE";

let initialState = {
    groups: [],
    totalCount: 0,
    currentPage: 0,
    pageSize: 20
}

const groupsReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_GROUPS: {
            return setGroupsAction(state, action);
        }
        case SET_CURRENT_PAGE: {
            return setCurrentPageAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const setGroupsAction = (state, action) => {
    return {
        ...state,
        groups: action.groups,
        totalCount: action.totalCount
    }
}

const setCurrentPageAction = (state, action) => {
    return {
        ...state,
        currentPage: action.currentPage
    }
}

export const setGroups = (groups, totalCount) => ({
    type: SET_GROUPS,
    groups,
    totalCount
});

export const setCurrentPage = (currentPage) => ({
    type: SET_CURRENT_PAGE,
    currentPage
});

export const getAllGroups = (page, pageSize) => (dispatch) => {
    groupApi.getFirstLevelGroups(page, pageSize)
        .then(response => {
            dispatch(setGroups(response.data, response.headers['content-range']));
        });
}

export default groupsReducer;