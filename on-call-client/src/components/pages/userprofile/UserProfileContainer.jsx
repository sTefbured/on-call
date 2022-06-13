import {compose} from "redux";
import {connect} from "react-redux";
import {getUserById} from "../../../redux/reducers/userProfileReducer";
import UserProfile from "./UserProfile";
import React from "react";
import {findOrCreateChat} from "../../../redux/reducers/chatReducer";

class UserProfileContainer extends React.Component {
    state = {
        navigateToDialog: false
    }

    setNavigateToDialog(value) {
        this.setState({navigateToDialog: value});
    }

    loadChat(userId) {
        this.props.findOrCreateChat(userId)
            .then(() => this.setNavigateToDialog(true));
    }

    render() {
        return (
            <UserProfile {...this.props} loadChat={(userId) => this.loadChat(userId)} navigateToDialog={this.state.navigateToDialog}/>
        );
    }
}

let mapStateToProps = (state) => ({
    user: state.userProfilePage.user,
    chat: state.chat.chat,
    authorizedUser: state.auth.user
})

export default compose(
    connect(mapStateToProps, {
        getUserById,
        findOrCreateChat
    })
)(UserProfileContainer);