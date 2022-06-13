import React from "react";
import {compose} from "redux";
import {connect} from "react-redux";
import {logout} from "../../../../redux/reducers/authReducer";
import UserMenu from "./UserMenu";

let mapStateToProps = (state) => ({
    user: state.auth.user
})

export default compose(
    connect(mapStateToProps, {
        logout
    })
)(UserMenu);