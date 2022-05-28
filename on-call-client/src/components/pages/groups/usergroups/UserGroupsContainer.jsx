import {connect} from "react-redux";
import React, {useEffect, useState} from "react";
import {getAllGroupsForUser, setCurrentPage} from "../../../../redux/reducers/groupsReducer";
import UserGroups from "./UserGroups";

const UserGroupsContainer = (props) => {
    let [isNewGroupDialogOpened, setIsNewGroupDialogOpened] = useState(false);
    let onPageChanged = (pageNumber) => {
        props.setCurrentPage(pageNumber);
        props.getAllGroupsForUser(props.authorizedUser.id, pageNumber, props.pageSize);
    }

    useEffect(() => {
        if (props.isAuthorized) {
            props.getAllGroupsForUser(props.authorizedUser.id, props.currentPage, props.pageSize);
        }
    }, [props.isAuthorized]);

    return (
        <UserGroups groups={props.groups} totalCount={props.totalCount} pageSize={props.pageSize}
                    setIsNewGroupDialogOpened={(isOpened) => setIsNewGroupDialogOpened(isOpened)}
                    isNewGroupDialogOpened={isNewGroupDialogOpened} onPageChanged={(n) => onPageChanged(n)}/>
    );
}

let mapStateToProps = (state) => ({
    authorizedUser: state.auth.user,
    isAuthorized: state.auth.isAuthorized,
    groups: state.group.groups,
    totalCount: state.group.totalCount,
    currentPage: state.group.currentPage,
    pageSize: state.group.pageSize
});

export default connect(mapStateToProps, {
    getAllGroupsForUser,
    setCurrentPage
})(UserGroupsContainer);