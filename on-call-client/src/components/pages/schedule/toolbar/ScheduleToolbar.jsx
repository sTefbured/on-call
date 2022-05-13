import styles from "./scheduletoolbar.module.css";
import ScheduleRecordCreationDialogContainer from "./creationdialog/ScheduleRecordCreationDialogContainer";
import Button from "../../../common/button/Button";

const ScheduleToolbar = (props) => {
    return (
        <div className={styles.scheduleToolbar}>
            <ScheduleRecordCreationDialogContainer isActive={props.isAddingNewEvent}
                                                   setIsActive={(isActive) => props.setIsAddingNewEvent(isActive)}
                                                   authorizedUser={props.authorizedUser}/>
            <Button onClick={() => props.openCreationDialog()}>Add new event</Button>
        </div>
    );
}

export default ScheduleToolbar;