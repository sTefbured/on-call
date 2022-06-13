import React from "react";
import {connect} from "react-redux";
import {
    createScheduleRecord,
    setDescription,
    setEventDateTime,
    setName
} from "../../../../../redux/reducers/scheduleReducer";
import ScheduleRecordCreationDialog from "./ScheduleRecordCreationDialog";

let mapStateToProps = (state) => ({
    scheduleRecord: state.schedule.scheduleRecord,
    from: state.schedule.from,
});

export default connect(mapStateToProps, {
    setName,
    setDescription,
    setEventDateTime,
    createScheduleRecord
})(ScheduleRecordCreationDialog);