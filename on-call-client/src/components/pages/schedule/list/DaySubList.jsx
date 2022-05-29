import styles from "./schedulelist.module.css";

const DaySubList = (props) => {
    let day = props.day.representation;
    return (
        <div>
            <div className={styles.dayOfWeek}>
                {day}
            </div>
            <div>
                {props.day.eventRepresentations}
            </div>
        </div>
    )
}

export default DaySubList;