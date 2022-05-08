import React from "react";
import LoginForm from "./LoginForm";
import {compose} from "redux";
import {connect} from "react-redux";
import {login, setPassword, setUsername} from "../../../redux/reducers/authReducer";
import {withUserProfileRedirect} from "../../../hoc/withUserProfileRedirect";

class LoginFormContainer extends React.Component {
    render() {
        return (
            <LoginForm {...this.props}/>
        );
    }
}

const mapStateToProps = (state) => ({
    username: state.auth.username,
    password: state.auth.password,
});

export default compose(
    withUserProfileRedirect,
    connect(mapStateToProps, {
        setUsername,
        setPassword,
        login
    })
)(LoginFormContainer)