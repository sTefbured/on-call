import styles from "../../videoconferencerooms.module.css"
import VideoconferenceRoomRow from "./VideoconferenceRoomRow";
import Button from "../../../../common/button/Button";
import DialogPopup from "../../../../common/dialog/DialogPopup";
import TextInput from "../../../../common/textinput/TextInput";
import {useState} from "react";

const UserVideoconferenceRoomsList = (props) => {
    let [isDialogActive, setIsDialogActive] = useState(false);
    let [name, setName] = useState("");
    let [accessCode, setAccessCode] = useState("");
    let rooms = props.conferenceRooms.map(room =>
        <VideoconferenceRoomRow key={room.id} conferenceRoom={room}/>
    );

    return (
        <div>
            <DialogPopup title="Create conference room" className={styles.dialog} isActive={isDialogActive}
                         setIsActive={(isActive) => setIsDialogActive(isActive)}>
                <div>
                    <label>Name</label>
                    <TextInput value={name} onChange={e => setName(e.target.value)}/>
                </div>
                <div>
                    <label>Access code</label>
                    <TextInput value={accessCode} onChange={e => setAccessCode(e.target.value)}/>
                </div>
                <Button onClick={() => {
                    props.createVideoconferenceRoom({name, accessCode})
                }}>Create</Button>
            </DialogPopup>
            <Button onClick={() => setIsDialogActive(true)}>Create new room</Button>
            <div className={styles.roomsContainer}>
                {rooms}
            </div>
        </div>
    );
}

export default UserVideoconferenceRoomsList;