import React from "react";
import {useParams} from "react-router-dom";
import ConferenceRoomContainer from "./ConferenceRoomContainer";

//TODO: add validation and password input
const VideoConference = () => {
    let {id} = useParams()
    return (
        <ConferenceRoomContainer roomId={id}/>
    )
}

export default VideoConference;