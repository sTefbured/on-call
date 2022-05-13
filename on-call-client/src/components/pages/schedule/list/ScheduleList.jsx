import styles from "./schedulelist.module.css";
import DaySubList from "./DaySubList";
import Button from "../../../common/button/Button";
import ScheduleRecordRowContainer from "./row/ScheduleRecordRowContainer";
import ScheduleRecordDialog from "../schedulerecord/ScheduleRecordDialog";

const ScheduleList = (props) => {
    let weekSchedule = {
        days: [],

        getDay(number) {
            for (let i = 0; i < this.days.length; i++) {
                if (this.days[i].number === number) {
                    return this.days[i];
                }
            }
        }
    };
    let currentDate = new Date(props.from);
    for (let i = 0; i < 7; i++) {
        let dayName = currentDate.toLocaleDateString('en-US', {weekday: "long"});
        let day = {
            number: currentDate.getDay(),
            representation: <h4>{dayName}</h4>,
            eventRepresentations: []
        };
        currentDate.setDate(currentDate.getDate() + 1);
        weekSchedule.days = [...weekSchedule.days, day];
    }

    props.scheduleRecords.forEach(record => {
        let date = new Date(record.eventDateTime);
        weekSchedule.getDay(date.getDay())
            .eventRepresentations
            .push(<ScheduleRecordRowContainer key={record.id}
                                              scheduleRecord={record}
                                              setIsScheduleRecordOpen={props.setIsRecordOpened}/>);
    });
    let schedule = weekSchedule.days.map(day => <DaySubList key={day.number} day={day}/>);
    return (
        <div className={styles.schedule}>
            <ScheduleRecordDialog isOpened={props.isRecordOpened}
                                  setIsOpened={props.setIsRecordOpened}
                                  scheduleRecord={props.scheduleRecord}/>
            <div className={styles.changeWeekToolbar}>
                <Button className={styles.changeWeekButton} onClick={props.previousWeek}>Previous week</Button>
                <Button className={styles.changeWeekButton} onClick={props.nextWeek}>Next week</Button>
            </div>
            <div className={styles.scheduleList}>
                {schedule}
            </div>
        </div>
    );
}

export default ScheduleList;