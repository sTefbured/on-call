import styles from "../../videoconferencerooms.module.css"
import VideoconferenceRoomRow from "./VideoconferenceRoomRow";

const UserVideoconferenceRoomsList = (props) => {
    let rooms = props.conferenceRooms.map(room =>
        <VideoconferenceRoomRow key={room.id} conferenceRoom={room}/>
    );

    return (
        <div className={styles.roomsContainer}>
            {rooms}
        </div>
    );
}

export default UserVideoconferenceRoomsList;