import styles from "../../videoconferencerooms.module.css";
import {NavLink} from "react-router-dom";

const VideoconferenceRoomRow = (props) => {
    return (
        <NavLink to={"/conferences/" + props.conferenceRoom.id}>
            <div className={styles.listRow}>
                <h3>{props.conferenceRoom.name}</h3>
            </div>
        </NavLink>
    );
}

export default VideoconferenceRoomRow;