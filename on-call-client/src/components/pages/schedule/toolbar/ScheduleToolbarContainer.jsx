import React from "react";
import {connect} from "react-redux";
import ScheduleToolbar from "./ScheduleToolbar";

class ScheduleToolbarContainer extends React.Component {
    state = {
        isAddingNewEvent: false
    }

    setIsAddingNewEvent(isAddingNewEvent) {
        this.setState({isAddingNewEvent: isAddingNewEvent});
    }

    render() {
        return (
            <ScheduleToolbar isAddingNewEvent={this.state.isAddingNewEvent}
                             setIsAddingNewEvent={(isAddingNewEvent) => this.setIsAddingNewEvent(isAddingNewEvent)}
                             authorizedUser={this.props.authorizedUser}/>
        );
    }
}

let mapStateToProps = (state) => ({
    authorizedUser: state.auth.user
});

export default connect(mapStateToProps)(ScheduleToolbarContainer);