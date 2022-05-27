import {groupApi} from "../../api/groupApi";

export const SET_GROUPS = "SET_GROUPS";
export const SET_GROUP = "SET_GROUP";
export const SET_CURRENT_PAGE = "SET_CURRENT_PAGE";
export const SET_IS_MEMBER = "SET_IS_MEMBER";
export const SET_JOIN_REQUEST_MESSAGE = "SET_JOIN_REQUEST_MESSAGE";
export const SET_JOIN_GROUP_REQUESTS = "SET_JOIN_GROUP_REQUESTS";
export const SET_TOTAL_COUNT = "SET_TOTAL_COUNT";

let initialState = {
    groups: [],
    group: null,
    joinGroupRequests: [],
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
        case SET_JOIN_GROUP_REQUESTS: {
            return setJoinGroupRequestsAction(state, action);
        }
        case SET_TOTAL_COUNT: {
            return setTotalCountAction(state, action);
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

const setJoinGroupRequestsAction = (state, action) => ({
    ...state,
    joinGroupRequests: action.joinGroupRequests
});

const setTotalCountAction = (state, action) => ({
    ...state,
    totalCount: action.totalCount
});

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

export const setJoinGroupRequests = (joinGroupRequests) => ({
    type: SET_JOIN_GROUP_REQUESTS,
    joinGroupRequests
});

export const setTotalCount = (totalCount) => ({
    type: SET_TOTAL_COUNT,
    totalCount
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

export const getAllGroupsForUser = (userId, page, pageSize) => (dispatch) => {
    groupApi.getAllGroupsForUser(userId, page, pageSize)
        .then(response => {
            dispatch(setGroups(response.data, response.headers['content-range']));
        });
}

export const getGroupById = (groupId) => (dispatch) => {
    groupApi.getGroupById(groupId)
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

export const getAllJoinRequests = (groupId, page, pageSize) => (dispatch) => {
    groupApi.getAllJoinRequests(groupId, page, pageSize)
        .then(response => {
            dispatch(setTotalCount(response.headers["content-range"]));
            dispatch(setJoinGroupRequests(response.data));
        });
}

export const approveJoinRequest = (request, currentPage, pageSize) => (dispatch) => {
    groupApi.approveJoinRequest(request)
        .then(() => {
            dispatch(getAllJoinRequests(request.group.id, currentPage, pageSize));
        });
}

export default groupsReducer;