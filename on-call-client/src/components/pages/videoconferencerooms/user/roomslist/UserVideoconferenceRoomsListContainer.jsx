import React from "react";
import {connect} from "react-redux";
import UserVideoconferenceRoomsList from "./UserVideoconferenceRoomsList";
import {getVideoconferenceRoomsForUser, setPage} from "../../../../../redux/reducers/videoconferenceReducer";

class UserVideoconferenceRoomsListContainer extends React.Component {
    componentDidMount() {
        if (this.props.authorizedUser?.id) {
            this.props.getVideoconferenceRoomsForUser(this.props.authorizedUser.id, this.props.page, this.props.pageSize);
        }
        this.props.setPage(0);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.authorizedUser !== this.props.authorizedUser) {
            this.props.getVideoconferenceRoomsForUser(this.props.authorizedUser.id, this.props.page, this.props.pageSize);
        }
    }

    render() {
        return (
            <UserVideoconferenceRoomsList {...this.props}/>
        );
    }
}

let mapStateToProps = (state) => ({
    conferenceRooms: state.videoconference.conferenceRooms,
    page: state.videoconference.page,
    pageSize: state.videoconference.pageSize,
    authorizedUser: state.auth.user
});

export default connect(mapStateToProps, {
    getVideoconferenceRoomsForUser,
    setPage
})(UserVideoconferenceRoomsListContainer);