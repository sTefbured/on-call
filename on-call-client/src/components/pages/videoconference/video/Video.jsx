import styles from "../videoconference.module.css";
import {useEffect, useRef} from "react";

const Video = (props) => {
    let videoRef = useRef();
    useEffect(() => {
        if (props.videoStream) {
            videoRef.current.srcObject = props.videoStream;
        }
    }, [props.videoStream]);
    return (
        <video className={styles.mirrored + ' ' + (props.id ? props.id : 'none')}
               autoPlay
               playsInline
               muted={props.isMuted}
               ref={videoRef}/>
    );
}

export default Video;