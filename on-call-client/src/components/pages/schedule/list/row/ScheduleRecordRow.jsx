import styles from "../schedulelist.module.css";

const ScheduleRecordRow = (props) => {
    return (
        <div className={styles.scheduleListRow} onClick={() => props.openScheduleRecord(props.scheduleRecord.id)}>
            <div>{props.scheduleRecord.name}</div>
            <div>{props.scheduleRecord.eventDateTime}</div>
        </div>
    )
}

export default ScheduleRecordRow;