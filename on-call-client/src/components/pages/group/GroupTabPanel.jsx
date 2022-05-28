import styles from "./group.module.css";
import TabItem from "./TabItem";
import {
    ADD_MEMBER_PERMISSION,
    GROUP_MEMBER_VIEW_PERMISSION,
    hasUserPermissionForGroup
} from "../../../utils/onCallUtils";

const GroupTabPanel = (props) => {
    if (!props.group || !props.isAuthorized) {
        return <></>
    }
    return (
        <div className={styles.tabs}>
            <TabItem to={"/groups" + props.group.idTag}>Group information</TabItem>
            <TabItem to={"/groups" + props.group.idTag + "?tab=members"}>Members</TabItem>
            <TabItem to={"/groups" + props.group.idTag + "?tab=subgroups"}>Subgroups</TabItem>
            {
                hasUserPermissionForGroup(props.authorizedUser, GROUP_MEMBER_VIEW_PERMISSION, props.group.id)
                    ? <TabItem to={"/groups" + props.group.idTag + "?tab=chats"}>Chats</TabItem>
                    : <></>
            }
            {
                hasUserPermissionForGroup(props.authorizedUser, ADD_MEMBER_PERMISSION, props.group.id)
                    ? <TabItem to={"/groups" + props.group.idTag + "?tab=join_requests"}>Join requests</TabItem>
                    : <></>
            }
        </div>
    );
}

export default GroupTabPanel;