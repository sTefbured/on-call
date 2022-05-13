import DialogPopup from "../../../common/dialog/DialogPopup";

const ScheduleRecordDialog = (props) => {
    return (
        <DialogPopup isActive={props.isOpened} setIsActive={props.setIsOpened} title={props.scheduleRecord.name}>
            <p>{props.scheduleRecord.description}</p>
            <div>{props.scheduleRecord.eventDateTime}</div>
            {/*<div>{props.scheduleRecord.creator.firstName + ' ' + props.scheduleRecord.creator.lastName}</div>*/}
        </DialogPopup>
    )
}

export default ScheduleRecordDialog;