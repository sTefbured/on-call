import {connect} from "react-redux";
import ConferenceRoom from "./ConferenceRoom";
import {getStunServers, getVideoconferenceRoomById} from "../../../redux/reducers/videoconferenceReducer";
import React from "react";

class ConferenceRoomContainer extends React.Component {
    render() {
        return (
            <ConferenceRoom {...this.props}/>
        )
    }
}

let mapStateToProps = (state) => ({
    conferenceRoom: state.videoconference.conferenceRoom,
    isAuthorized: state.auth.isAuthorized,
    authorizedUser: state.auth.user
});

export default connect(mapStateToProps, {
    getVideoconferenceRoomById,
    getStunServers
})(ConferenceRoomContainer);