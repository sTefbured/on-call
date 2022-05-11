import React from "react";
import {connect} from "react-redux";
import {
    addScheduleRecord,
    setDescription,
    setEventDateTime,
    setName,
    setUser
} from "../../../../../redux/reducers/scheduleReducer";
import ScheduleRecordCreationDialog from "./ScheduleRecordCreationDialog";

let mapStateToProps = (state) => ({
    scheduleRecord: state.schedule.scheduleRecord
});

export default connect(mapStateToProps, {
    setName,
    setDescription,
    setEventDateTime,
    setUser,
    addScheduleRecord
})(ScheduleRecordCreationDialog);