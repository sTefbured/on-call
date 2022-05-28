import {useLocation, useSearchParams} from "react-router-dom";
import {useEffect} from "react";
import {connect} from "react-redux";
import {getGroupById, getGroupByTagSequence, setGroup} from "../../../redux/reducers/groupsReducer";
import JoinRequestsContainer from "./joinrequest/JoinRequestsContainer";
import {ADD_MEMBER_PERMISSION, hasUserPermissionForGroup} from "../../../utils/onCallUtils";
import Group from "./Group";
import GroupInformationContainer from "./groupinformation/GroupInformationContainer";
import GroupChatsTabContainer from "./chats/GroupChatsTabContainer";
import GroupMembersTabContainer from "./members/GroupMembersTabContainer";
import SubgroupsTabContainer from "./subgroups/SubgroupsTabContainer";

const GroupContainer = (props) => {
    let location = useLocation();

    useEffect(async () => {
        let tagSequence = location.pathname.replace('/groups/', '');
        isNaN(parseInt(tagSequence))
            ? props.getGroupByTagSequence(tagSequence)
            : props.getGroupById(tagSequence)
        return () => props.setGroup(null);
    }, [location]);

    let [params] = useSearchParams();
    let content = <GroupInformationContainer/>;
    switch (params.get("tab")) {
        case "join_requests": {
            if (props.group
                && props.isAuthorized
                && hasUserPermissionForGroup(props.authorizedUser, ADD_MEMBER_PERMISSION, props.group.id)) {
                content = <JoinRequestsContainer/>
            }
            break;
        }
        case "chats": {
            content = <GroupChatsTabContainer/>
            break;
        }
        case "members": {
            content = <GroupMembersTabContainer/>
            break;
        }
        case "subgroups": {
            content = <SubgroupsTabContainer/>
            break;
        }
    }
    return (
        <Group authorizedUser={props.authorizedUser} isAuthorized={props.isAuthorized} group={props.group}>
            {content}
        </Group>
    );
}

let mapStateToProps = (state) => ({
    authorizedUser: state.auth.user,
    isAuthorized: state.auth.isAuthorized,
    group: state.group.group
});

export default connect(mapStateToProps, {
    getGroupByTagSequence,
    getGroupById,
    setGroup
})(GroupContainer);