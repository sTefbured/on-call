import {applyMiddleware, combineReducers, createStore} from 'redux';
import registrationReducer from "./reducers/registrationReducer";
import usersReducer from "./reducers/usersReducer";
import thunkMiddleware from "redux-thunk"
import userProfileReducer from "./reducers/userProfileReducer";
import authReducer from "./reducers/authReducer";

let reducers = combineReducers({
    registrationPage: registrationReducer,
    usersPage: usersReducer,
    userProfilePage: userProfileReducer,
    auth: authReducer
});
let store = createStore(reducers, applyMiddleware(thunkMiddleware));

export default store