import {groupApi} from "../../api/groupApi";

export const SET_GROUPS = "SET_GROUPS";
export const SET_GROUP = "SET_GROUP";
export const SET_CURRENT_PAGE = "SET_CURRENT_PAGE";
export const SET_IS_MEMBER = "SET_IS_MEMBER";
export const SET_JOIN_REQUEST_MESSAGE = "SET_JOIN_REQUEST_MESSAGE";

let initialState = {
    groups: [],
    group: null,
    joinGroupRequest: {
        message: ""
    },
    isMember: false,
    totalCount: 0,
    currentPage: 0,
    pageSize: 20
}

const groupsReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_GROUPS: {
            return setGroupsAction(state, action);
        }
        case SET_GROUP: {
            return setGroupAction(state, action);
        }
        case SET_CURRENT_PAGE: {
            return setCurrentPageAction(state, action);
        }
        case SET_IS_MEMBER: {
            return setIsMemberAction(state, action);
        }
        case SET_JOIN_REQUEST_MESSAGE: {
            return setJoinRequestMessageAction(state, action);
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

const setGroupAction = (state, action) => {
    return {
        ...state,
        group: action.group
    }
}

const setCurrentPageAction = (state, action) => {
    return {
        ...state,
        currentPage: action.currentPage
    }
}

const setIsMemberAction = (state, action) => {
    return {
        ...state,
        isMember: action.isMember
    }
}

const setJoinRequestMessageAction = (state, action) => {
    return {
        ...state,
        joinGroupRequest: {
            message: action.message
        }
    }
}

export const setGroups = (groups, totalCount) => ({
    type: SET_GROUPS,
    groups,
    totalCount
});

export const setGroup = (group) => ({
    type: SET_GROUP,
    group
});

export const setCurrentPage = (currentPage) => ({
    type: SET_CURRENT_PAGE,
    currentPage
});

export const setIsMember = (isMember) => ({
    type: SET_IS_MEMBER,
    isMember
});

export const setJoinRequestMessage = (message) => ({
    type: SET_JOIN_REQUEST_MESSAGE,
    message
});

export const getAllGroups = (page, pageSize) => (dispatch) => {
    groupApi.getFirstLevelGroups(page, pageSize)
        .then(response => {
            dispatch(setGroups(response.data, response.headers['content-range']));
        });
}

export const getGroupByTagSequence = (tagSequence) => (dispatch) => {
    groupApi.getGroupByTagSequence(tagSequence)
        .then(response => {
            dispatch(setIsMember(response.data.isMember));
            dispatch(setGroup(response.data));
        });
}

export const createJoinRequest = (groupId, user, message) => () => {
    let request = {
        message: message,
        user: user,
        group: {
            id: groupId
        }
    }
    groupApi.createJoinRequest(request);
}

export default groupsReducer;