import {connect} from "react-redux";
import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import GroupInfoCard from "../GroupInfoCard";
import React, {useEffect} from "react";
import {getAllGroupsForUser, setCurrentPage} from "../../../../redux/reducers/groupsReducer";

const UserGroupsContainer = (props) => {
    let groupComponents = props.groups.map(group => (<div key={group.id}><GroupInfoCard group={group}/></div>));
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
        <CollectionGrid totalCount={props.totalCount}
                        pageSize={props.pageSize}
                        currentPage={props.currentPage}
                        onPageChanged={n => onPageChanged(n)}>
            {groupComponents}
        </CollectionGrid>
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