import {useLocation, useNavigate, useSearchParams} from "react-router-dom";
import {useEffect} from "react";
import {connect} from "react-redux";
import {getGroupById, getGroupByTagSequence} from "../../../redux/reducers/groupsReducer";
import JoinRequestsContainer from "./joinrequest/JoinRequestsContainer";
import {ADD_MEMBER_PERMISSION, hasUserPermissionForGroup} from "../../../utils/onCallUtils";
import Group from "./Group";
import GroupInformationContainer from "./groupinformation/GroupInformationContainer";
import GroupChatsTabContainer from "./chats/GroupChatsTabContainer";

const GroupContainer = (props) => {
    let location = useLocation();
    let navigate = useNavigate();

    useEffect(async () => {
        let tagSequence = location.pathname.replace('/groups/', '');
        isNaN(parseInt(tagSequence))
            ? props.getGroupByTagSequence(tagSequence)
            : props.getGroupById(tagSequence)
    }, []);

    useEffect(() => {
        if (!props.group) {
            return;
        }

        let idTagPath = "/groups" + props.group.idTag;
        if (!location.pathname.startsWith(idTagPath)) {
            navigate(idTagPath + location.search, {replace: true});
        }
    }, [props.group, location]);

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
    getGroupById
})(GroupContainer);