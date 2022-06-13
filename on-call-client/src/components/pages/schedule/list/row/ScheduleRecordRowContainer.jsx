import React from "react";
import {connect} from "react-redux";
import ScheduleRecordRow from "./ScheduleRecordRow";
import {getScheduleRecord} from "../../../../../redux/reducers/scheduleReducer";

class ScheduleRecordRowContainer extends React.Component {
    openScheduleRecord(recordId) {
        this.props.getScheduleRecord(recordId);
        this.props.setIsScheduleRecordOpen(true);
    }

    render() {
        return (
            <ScheduleRecordRow {...this.props} openScheduleRecord={(recordId) => this.openScheduleRecord(recordId)}/>
        );
    }
}

let mapStateToProps = (state) => ({
});

export default connect(mapStateToProps, {
    getScheduleRecord
})(ScheduleRecordRowContainer);