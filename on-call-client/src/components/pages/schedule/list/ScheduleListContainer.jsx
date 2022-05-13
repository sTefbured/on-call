import React from "react";
import {connect} from "react-redux";
import ScheduleList from "./ScheduleList";
import {getUserScheduleRecords, setFrom, setScheduleRecord} from "../../../../redux/reducers/scheduleReducer";

class ScheduleListContainer extends React.Component{
    state = {
        isRecordOpened: false
    }

    setIsRecordOpened(isRecordOpened) {
        this.setState({isRecordOpened: isRecordOpened});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.from !== this.props.from || prevProps.authorizedUser.id !== this.props.authorizedUser.id) {
            this.props.getUserScheduleRecords(this.props.authorizedUser.id, this.props.from);
        }
    }

    nextWeek() {
        let newFrom = new Date(this.props.from);
        newFrom.setDate(newFrom.getDate() + 7);
        newFrom.setHours(0, -newFrom.getTimezoneOffset(), 0, 0);
        this.props.setFrom(newFrom.toISOString().replace('Z', ''));
    }

    previousWeek() {
        let newFrom = new Date(this.props.from);
        newFrom.setDate(newFrom.getDate() - 7);
        newFrom.setHours(0, -newFrom.getTimezoneOffset(), 0, 0);
        this.props.setFrom(newFrom.toISOString().replace('Z', ''));
    }

    render() {
        return (
            <ScheduleList {...this.props}
                          isRecordOpened={this.state.isRecordOpened}
                          nextWeek={() => this.nextWeek()}
                          previousWeek={() => this.previousWeek()}
                          setIsRecordOpened={(isRecordOpened) => this.setIsRecordOpened(isRecordOpened)}/>
        );
    }
}

let mapStateToProps = (state) => ({
    scheduleRecords: state.schedule.scheduleRecords,
    scheduleRecord: state.schedule.scheduleRecord,
    authorizedUser: state.auth.user,
    from: state.schedule.from,
});

export default connect(mapStateToProps, {
    getUserScheduleRecords,
    setFrom,
    setScheduleRecord
})(ScheduleListContainer);