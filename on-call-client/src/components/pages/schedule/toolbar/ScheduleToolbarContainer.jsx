import React from "react";
import {connect} from "react-redux";
import ScheduleToolbar from "./ScheduleToolbar";
import {setScheduleRecord} from "../../../../redux/reducers/scheduleReducer";

class ScheduleToolbarContainer extends React.Component {
    state = {
        isAddingNewEvent: false
    }

    setIsAddingNewEvent(isAddingNewEvent) {
        this.setState({isAddingNewEvent: isAddingNewEvent});
    }

    openCreationDialog(userId) {
        this.props.setScheduleRecord({
            id: null,
            eventDateTime: '',
            name: '',
            description: '',
            user: {
                id: userId
            },
            group: null
        });
        this.setIsAddingNewEvent(true);
    }

    render() {
        return (
            <ScheduleToolbar isAddingNewEvent={this.state.isAddingNewEvent}
                             setIsAddingNewEvent={(isAddingNewEvent) => this.setIsAddingNewEvent(isAddingNewEvent)}
                             authorizedUser={this.props.authorizedUser}
                             openCreationDialog={() => this.openCreationDialog(this.props.authorizedUser.id)}/>
        );
    }
}

let mapStateToProps = (state) => ({
    authorizedUser: state.auth.user
});

export default connect(mapStateToProps, {
    setScheduleRecord
})(ScheduleToolbarContainer);