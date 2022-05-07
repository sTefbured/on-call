import {compose} from "redux";
import {connect} from "react-redux";
import {getUserById} from "../../../redux/reducers/userProfileReducer";
import UserProfile from "./UserProfile";
import React from "react";

class UserProfileContainer extends React.Component {
    render() {
        return (
            <UserProfile {...this.props}/>
        );
    }
}

let mapStateToProps = (state) => ({
    user: state.userProfilePage.user
})

export default compose(
    connect(mapStateToProps, {
        getUserById
    })
)(UserProfileContainer);