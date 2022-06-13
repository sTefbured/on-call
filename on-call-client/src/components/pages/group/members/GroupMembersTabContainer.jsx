import {connect} from "react-redux";
import {getGroupMembers, setCurrentPage} from "../../../../redux/reducers/groupsReducer";
import {useEffect} from "react";
import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import UserInfoCard from "../../users/userslist/UserInfoCard";

const GroupMembersTabContainer = (props) => {
    useEffect(() => {
        if (props.group) {
            props.getGroupMembers(props.group.id, props.currentPage, props.pageSize);
        }
    }, [props.group, props.page]);
    let memberComponents = props.members.map(member => <UserInfoCard user={member} key={member.id}/>);
    return (
        <CollectionGrid totalCount={props.totalCount}
                        pageSize={props.pageSize}
                        currentPage={props.currentPage}
                        onPageChanged={n => props.setCurrentPage(n)}>
            {memberComponents}
        </CollectionGrid>
    );
}

let mapStateToProps = (state) => ({
    members: state.group.members,
    group: state.group.group,
    currentPage: state.group.currentPage,
    pageSize: state.group.pageSize,
    totalCount: state.group.totalCount
});

export default connect(mapStateToProps, {
    getGroupMembers,
    setCurrentPage
})(GroupMembersTabContainer);