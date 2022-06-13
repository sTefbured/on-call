import React from "react";
import {
    changeAvatar,
    changeBirthDate,
    changeEmail,
    changeFirstName,
    changeLastName,
    changePassword,
    changeUsername,
    register
} from "../../../../redux/reducers/registrationReducer";
import UserParametersForm from "./UserParametersForm";
import {connect} from "react-redux";
import {Navigate} from "react-router-dom";

class UserParametersFormContainer extends React.Component {
    render() {
        return this.props.hasRegistered
            ? (<Navigate to="/login"/>)
            : (<UserParametersForm {...this.props}/>)
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.registrationPage.user,
        avatar: state.registrationPage.avatar,
        hasRegistered: state.registrationPage.hasRegistered
    }
}

export default connect(mapStateToProps, {
    register,
    changeFirstName,
    changeLastName,
    changeUsername,
    changeEmail,
    changeAvatar,
    changePassword,
    changeBirthDate
})(UserParametersFormContainer);