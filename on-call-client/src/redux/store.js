import {applyMiddleware, combineReducers, createStore} from 'redux';
import thunkMiddleware from "redux-thunk"
import registrationReducer from "./reducers/registrationReducer";
import usersReducer from "./reducers/usersReducer";
import userProfileReducer from "./reducers/userProfileReducer";
import authReducer from "./reducers/authReducer";
import groupsReducer from "./reducers/groupsReducer";
import notificationsReducer from "./reducers/notificationsReducer";
import scheduleReducer from "./reducers/scheduleReducer";
import videoconferenceReducer from "./reducers/videoconferenceReducer";

let reducers = combineReducers({
    registrationPage: registrationReducer,
    usersPage: usersReducer,
    userProfilePage: userProfileReducer,
    auth: authReducer,
    group: groupsReducer,
    notifications: notificationsReducer,
    schedule: scheduleReducer,
    videoconference: videoconferenceReducer
});
let store = createStore(reducers, applyMiddleware(thunkMiddleware));

window.store = store;

export default store