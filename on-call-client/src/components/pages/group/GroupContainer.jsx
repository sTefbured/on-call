import {useLocation, useNavigate, useSearchParams} from "react-router-dom";
import {useEffect} from "react";
import {connect} from "react-redux";
import {getGroupById, getGroupByTagSequence} from "../../../redux/reducers/groupsReducer";
import JoinRequestsContainer from "./joinrequest/JoinRequestsContainer";
import {hasUserPermissionForGroup} from "../../../utils/onCallUtils";
import Group from "./Group";

const ADD_MEMBER_PERMISSION = 61119559324084;

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
    if (params.get("tab") === "join_requests"
        && props.group
        && hasUserPermissionForGroup(props.authorizedUser, ADD_MEMBER_PERMISSION, props.group.id)) {
        return (
            <JoinRequestsContainer/>
        );
    }
    return (
        <Group group={props.group}/>
    );
}

let mapStateToProps = (state) => ({
    authorizedUser: state.auth.user,
    group: state.group.group
});

export default connect(mapStateToProps, {
    getGroupByTagSequence,
    getGroupById
})(GroupContainer);