import React, {createRef, useCallback, useEffect, useRef, useState} from "react";
import styles from './videoconference.module.css'

const getMediaStream = async (mediaConstraints) => {
    return await navigator.mediaDevices.getUserMedia(mediaConstraints)
}

const getIncomingMediaStreams = async (path) => {
    // TODO: add streams request from the server
    return [null, null];
}

const ConferenceRoom = () => {
    const [videos, addVideo] = useState([])
    useEffect(() => {
        getMediaStream({
            audio: true, video: {
                width: 500
            }
        })
            .then(mediaStream => {
                addVideo((prev) => {
                    return [...prev, <video className={`${styles.mirrored}`}
                                            autoPlay playsInline
                                            muted={true}
                                            ref={instance => {
                                                instance.srcObject = mediaStream
                                            }}/>]
                })
            });
    }, [])
    return (
        <div>
            {videos}
        </div>
    )
}

export default ConferenceRoom;