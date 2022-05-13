import styles from "./schedulelist.module.css";

const DaySubList = (props) => {
    return (
        <div>
            <div className={styles.dayOfWeek}>
                {props.day.representation}
            </div>
            <div>
                {props.day.eventRepresentations}
            </div>
        </div>
    )
}

export default DaySubList;