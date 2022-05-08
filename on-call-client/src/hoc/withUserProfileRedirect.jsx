import React from "react";
import {Navigate} from "react-router-dom";
import {connect} from "react-redux";

let mapStateToProps = (state) => ({
    isAuthorized: state.auth.isAuthorized,
    userId: state.auth?.user?.id
});

export const withUserProfileRedirect = (Component) => {
    class WrappedComponent extends React.Component {
        render() {
            return !this.props.isAuthorized
                ? <Component {...this.props}/>
                : <Navigate to={"/users/" + this.props.userId}/>
        }
    }

    return connect(mapStateToProps)(WrappedComponent);
}