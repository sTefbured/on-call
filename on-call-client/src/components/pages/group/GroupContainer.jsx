import GroupInformationContainer from "./groupinformation/GroupInformationContainer";
import {useLocation, useSearchParams} from "react-router-dom";
import {useEffect} from "react";
import {connect} from "react-redux";
import {getGroupByTagSequence} from "../../../redux/reducers/groupsReducer";
import JoinRequestsContainer from "./joinrequest/JoinRequestsContainer";
import {hasUserPermissionForGroup} from "../../../utils/onCallUtils";

const GroupContainer = (props) => {
    let location = useLocation()
    let tagSequence = location.pathname.replace('/groups/', '');
    let paramsStartIndex = tagSequence.indexOf("?");
    if (paramsStartIndex > 0) {
        tagSequence = tagSequence.substring(0, paramsStartIndex);
    }
    useEffect(() => props.getGroupByTagSequence(tagSequence), [location]);
    let [params] = useSearchParams();
    if (params.get("tab") === "join_requests"
        && props.group
        && hasUserPermissionForGroup(props.authorizedUser, 61119559324084, props.group.id)) {
        return (
            <JoinRequestsContainer/>
        );
    }
    return (
        <GroupInformationContainer/>
    );
}

let mapStateToProps = (state) => ({
    authorizedUser: state.auth.user,
    group: state.group.group
});

export default connect(mapStateToProps, {
    getGroupByTagSequence
})(GroupContainer);