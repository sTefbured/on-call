import React from "react";
import {connect} from "react-redux";
import {me} from "../../../redux/reducers/authReducer";
import Header from "./Header";

class HeaderContainer extends React.Component {
    componentDidMount() {
        this.props.me();
    }

    render() {
        return (
            <Header {...this.props}/>
        )
    }
}

let mapStateToProps = (state) => ({
    isAuthorized: state.auth.isAuthorized,
    user: state.auth.user
});

export default connect(mapStateToProps, {
    me
})(HeaderContainer);