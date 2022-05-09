import {applyMiddleware, combineReducers, createStore} from 'redux';
import registrationReducer from "./reducers/registrationReducer";
import usersReducer from "./reducers/usersReducer";
import thunkMiddleware from "redux-thunk"
import userProfileReducer from "./reducers/userProfileReducer";
import authReducer from "./reducers/authReducer";
import groupsReducer from "./reducers/groupsReducer";

let reducers = combineReducers({
    registrationPage: registrationReducer,
    usersPage: usersReducer,
    userProfilePage: userProfileReducer,
    auth: authReducer,
    group: groupsReducer
});
let store = createStore(reducers, applyMiddleware(thunkMiddleware));

window.store = store;

export default store