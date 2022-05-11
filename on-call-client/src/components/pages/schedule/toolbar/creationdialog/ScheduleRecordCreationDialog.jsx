import DialogPopup from "../../../../common/dialog/DialogPopup";
import styles from "../scheduletoolbar.module.css"
import TextInput from "../../../../common/textinput/TextInput";
import Button from "../../../../common/button/Button";
import TextArea from "../../../../common/textarea/TextArea";
import {useEffect} from "react";

const ScheduleRecordCreationDialog = (props) => {
    useEffect(() => {props.setUser(props.authorizedUser.id)}, [props.authorizedUser.id]);
    return (
        <DialogPopup isActive={props.isActive}
                     setIsActive={props.setIsActive}
                     title='Create schedule record'>
            <div className={styles.createDialog}>
                <div className={styles.createDialogRow}>
                    <label>Event name</label>
                    <TextInput className={styles.createDialogInputCell}
                               value={props.scheduleRecord.name}
                               onChange={(e) => props.setName(e.target.value)}/>
                </div>
                <div className={styles.createDialogRow}>
                    <label>Description</label>
                    <TextArea className={styles.createDialogInputCell}
                              onChange={(e) => props.setDescription(e.target.value)}>
                        {props.scheduleRecord.description}
                    </TextArea>
                </div>
                <div className={styles.createDialogRow}>
                    <label>Event date</label>
                    <TextInput className={styles.createDialogInputCell}
                               value={props.scheduleRecord.eventDateTime}
                               onChange={(e) => props.setEventDateTime(e.target.value)}
                               type='datetime-local'/>
                </div>
                <Button onClick={() => props.addScheduleRecord(props.scheduleRecord)}>Create</Button>
            </div>
        </DialogPopup>
    );
}

export default ScheduleRecordCreationDialog;