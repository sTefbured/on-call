import React from "react";
import ConferenceRoom from "./ConferenceRoom";
import {useParams} from "react-router-dom";

const getRoomInfo = (roomId) => {
    // TODO: add room information query
    return {}
}

const VideoConference = () => {
    let {roomId} = useParams()


    return (
        <div>
            <ConferenceRoom roomId={roomId}/>
        </div>
    )
}

export default VideoConference;