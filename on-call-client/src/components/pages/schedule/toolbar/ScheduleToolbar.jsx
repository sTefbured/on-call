import ScheduleRecordCreationDialogContainer from "./creationdialog/ScheduleRecordCreationDialogContainer";
import Button from "../../../common/button/Button";

const ScheduleToolbar = (props) => {
    return (
        <div>
            <ScheduleRecordCreationDialogContainer isActive={props.isAddingNewEvent}
                                                   setIsActive={(isActive) => props.setIsAddingNewEvent(isActive)}
                                                   authorizedUser={props.authorizedUser}/>
            <Button onClick={() => props.setIsAddingNewEvent(true)}>Add new event</Button>
        </div>
    );
}

export default ScheduleToolbar;