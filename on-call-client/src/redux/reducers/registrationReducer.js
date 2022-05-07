import {userApi} from "../../api/api";

export const CHANGE_USER_FIRST_NAME_ACTION = "CHANGE_USER_FIRST_NAME";
export const CHANGE_USER_LAST_NAME_ACTION = "CHANGE_USER_LAST_NAME";
export const CHANGE_USER_USERNAME_ACTION = "CHANGE_USER_USERNAME";
export const CHANGE_USER_EMAIL_ACTION = "CHANGE_USER_EMAIL";
export const CHANGE_USER_AVATAR_ACTION = "CHANGE_USER_AVATAR_ACTION";
export const CHANGE_USER_PASSWORD_ACTION = "CHANGE_USER_PASSWORD";
export const CHANGE_USER_BIRTH_DATE_ACTION = "CHANGE_USER_BIRTH_DATE";
export const CHANGE_HAS_REGISTERED_ACTION = "CHANGE_HAS_REGISTERED_ACTION";


let initialState = {
    user: {
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        password: "",
        birthDate: ""
    },
    avatar: "",
    hasRegistered: false
}

const registrationReducer = (state = initialState, action) => {
    switch (action.type) {
        case CHANGE_USER_FIRST_NAME_ACTION: {
            return changeUserFirstNameAction(state, action);
        }
        case CHANGE_USER_LAST_NAME_ACTION: {
            return changeUserLastNameAction(state, action);
        }
        case CHANGE_USER_USERNAME_ACTION: {
            return changeUserUsernameAction(state, action);
        }
        case CHANGE_USER_EMAIL_ACTION: {
            return changeUserEmailAction(state, action);
        }
        case CHANGE_USER_AVATAR_ACTION: {
            return changeUserAvatarAction(state, action);
        }
        case CHANGE_USER_PASSWORD_ACTION: {
            return changeUserPasswordAction(state, action);
        }
        case CHANGE_USER_BIRTH_DATE_ACTION: {
            return changeUserBirthDateAction(state, action);
        }
        case CHANGE_HAS_REGISTERED_ACTION: {
            return changeHasRegisteredAction(state, action);
        }
        default: {
            return state;
        }
    }
}

const changeUserFirstNameAction = (state, action) => {
    return {
        ...state,
        user: {
            ...state.user,
            firstName: action.firstName
        }
    };
}

const changeUserLastNameAction = (state, action) => {
    return {
        ...state,
        user: {
            ...state.user,
            lastName: action.lastName
        }
    };
}

const changeUserUsernameAction = (state, action) => {
    return {
        ...state,
        user: {
            ...state.user,
            username: action.username
        }
    };
}

const changeUserEmailAction = (state, action) => {
    return {
        ...state,
        user: {
            ...state.user,
            email: action.email
        }
    };
}

const changeUserAvatarAction = (state, action) => {
    let name = Math.random() * 10000000
    let renamedAvatar = new File([action.avatar], name.toString());
    return {
        ...state,
        avatar: renamedAvatar
    };
}

const changeUserPasswordAction = (state, action) => {
    return {
        ...state,
        user: {
            ...state.user,
            password: action.password
        }
    };
}

const changeUserBirthDateAction = (state, action) => {
    return {
        ...state,
        user: {
            ...state.user,
            birthDate: action.birthDate
        }
    };
}

const changeHasRegisteredAction = (state, action) => {
    return {
        ...state,
        hasRegistered: action.hasRegistered
    }
}

export const changeFirstName = (firstName) => ({
    type: CHANGE_USER_FIRST_NAME_ACTION,
    firstName: firstName
});

export const changeLastName = (lastName) => ({
    type: CHANGE_USER_LAST_NAME_ACTION,
    lastName: lastName
});

export const changeUsername = (username) => ({
    type: CHANGE_USER_USERNAME_ACTION,
    username: username
});

export const changeEmail = (email) => ({
    type: CHANGE_USER_EMAIL_ACTION,
    email: email
});

export const changeAvatar = (avatar) => ({
    type: CHANGE_USER_AVATAR_ACTION,
    avatar: avatar
});

export const changePassword = (password) => ({
    type: CHANGE_USER_PASSWORD_ACTION,
    password: password
});

export const changeBirthDate = (birthDate) => ({
    type: CHANGE_USER_BIRTH_DATE_ACTION,
    birthDate: birthDate
});

export const changeHasRegistered = (hasRegistered) => ({
    type: CHANGE_HAS_REGISTERED_ACTION,
    hasRegistered: hasRegistered
})

export const register = (user, avatar) => (dispatch) => {
    userApi.register(user, avatar)
        .then(response => {
            if (response.status === 201) {
                dispatch(changeHasRegistered(true));
            }
        });
}

export default registrationReducer;