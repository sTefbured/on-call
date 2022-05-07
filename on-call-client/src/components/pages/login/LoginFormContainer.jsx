import React from "react";
import LoginForm from "./LoginForm";
import {compose} from "redux";
import {connect} from "react-redux";
import {login, me, setPassword, setUsername} from "../../../redux/reducers/authReducer";
import {Navigate} from "react-router-dom";

class LoginFormContainer extends React.Component {
    componentDidMount() {
        this.props.me();
    }

    render() {
        return this.props.isAuthorized
            ? (<Navigate replace to="/users"/>)
            : (<LoginForm {...this.props}/>);
    }
}

const mapStateToProps = (state) => ({
    username: state.auth.username,
    password: state.auth.password,
    isAuthorized: state.auth.isAuthorized
});

export default compose(
    connect(mapStateToProps, {
        setUsername,
        setPassword,
        login,
        me
    })
)(LoginFormContainer)